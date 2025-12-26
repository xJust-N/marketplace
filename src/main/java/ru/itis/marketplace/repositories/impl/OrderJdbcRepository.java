package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.OrderItem;
import ru.itis.marketplace.models.enums.Status;
import ru.itis.marketplace.repositories.OrderRepository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderJdbcRepository implements OrderRepository<Long, Order> {

    private static final String SAVE_ORDER_QUERY = """
            insert into orders (user_id, created_at, status)
            values (?, ?, ?)
            returning order_id;
            """;
    private static final String FIND_ORDER_BY_ID_QUERY = """
            select * from orders where order_id = ? and status != 'shopping-cart';
            """;

    private static final String DELETE_ORDER_BY_ID_QUERY = """
            delete from orders where order_id = ?;
            """;
    private static final String UPDATE_ORDER_QUERY = """
            update orders
            set status = ?, created_at = ?
            where order_id = ?;
            """;
    private static final String SAVE_ORDER_PRODUCT_QUERY = """
            insert into orders_products (order_id, product_id, count)
            values (?, ?, ?);
            """;
    private static final String DELETE_ORDER_PRODUCTS_QUERY = """
            delete from orders_products where order_id = ?;
            """;
    private static final String FIND_ORDER_ITEMS_BY_ORDER_ID_QUERY = """
            select op.product_id, p.shop_id, p.product_name,
               op.count, p.price
            from orders_products op
            join products p on op.product_id = p.product_id
            where op.order_id = ?;
            """;
    private static final String FIND_CART_BY_USER_ID_QUERY = """
            select * from orders
            where user_id = ? and status = 'shopping-cart';
            """;
    private static final String FIND_ORDERS_BY_USER_ID_QUERY = """
            select * from orders
            where user_id = ? and status != 'shopping-cart'
            order by created_at desc;
            """;
    private static final String DELETE_CART_BY_USER_ID = """
            delete from orders
            where user_id = ? and status = 'shopping-cart';
            """;

    private final ConnectionHolder connectionHolder;
    private final BaseJdbcRepository<Long, Order> baseOrderRepository;

    public OrderJdbcRepository(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
        this.baseOrderRepository = new BaseJdbcRepository<>(connectionHolder, this::toOrder, Long.class);
    }


    @Override
    public Optional<Order> findById(Long id) throws SQLException {
        return baseOrderRepository.find(FIND_ORDER_BY_ID_QUERY, id);
    }

    @Override
    public Long save(Order order) throws SQLException {
        List<Object> params = List.of(
                order.getUserId(),
                order.getCreatedAt(),
                order.getStatus().getCode()
        );
        //Сохранение и удаление в транзакции для поддержания целостности и актуальности.
        //Сохраняются и удаляются данные из orders и orders_products
        Connection con = connectionHolder.getConnection();
        try {
            con.setAutoCommit(false);
            Long orderId = baseOrderRepository.saveWithGeneratedKey(SAVE_ORDER_QUERY, params, con);
            order.setId(orderId);
            saveOrderItems(orderId, order.getOrderItems(), con);
            con.commit();
            return orderId;
        } catch (Exception e) {
            if (con != null)
                con.rollback();
            throw new SQLException(e);
        } finally {
            if (con != null)
                con.close();
        }
    }

    @Override
    public void update(Order order) throws SQLException {
        List<Object> params = List.of(
                order.getStatus().getCode(),
                order.getCreatedAt(),
                order.getId()
        );
        Connection con = connectionHolder.getConnection();
        try {
            con.setAutoCommit(false);
            baseOrderRepository.update(UPDATE_ORDER_QUERY, params, con);
            deleteOrderItems(order.getId(), con);
            saveOrderItems(order.getId(), order.getOrderItems(), con);
            con.commit();
        } catch (Exception e) {
            if (con != null)
                con.rollback();
            throw new SQLException(e);
        } finally {
            if (con != null)
                con.close();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        Connection con = connectionHolder.getConnection();
        try {
            con.setAutoCommit(false);
            baseOrderRepository.deleteById(DELETE_ORDER_BY_ID_QUERY, id, con);
            deleteOrderItems(id, con);
            con.commit();
        } catch (Exception e) {
            if (con != null)
                con.rollback();
            throw new SQLException(e);
        } finally {
            if (con != null)
                con.close();
        }
    }

    @Override
    public Optional<Order> findCartByUserId(Long userId) throws SQLException {
        return baseOrderRepository.find(FIND_CART_BY_USER_ID_QUERY, userId);
    }

    @Override
    public void deleteCartByUserId(Long userId) throws SQLException {
        baseOrderRepository.deleteById(DELETE_CART_BY_USER_ID, userId);
    }

    @Override
    public List<Order> getByUserId(Long userId) throws SQLException {
       return baseOrderRepository.getAll(FIND_ORDERS_BY_USER_ID_QUERY, List.of(userId));
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        try (Connection con = connectionHolder.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ORDER_ITEMS_BY_ORDER_ID_QUERY)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = toOrderItem(rs);
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public void addOrderItem(Long orderId, OrderItem item) throws SQLException {
        try (Connection con = connectionHolder.getConnection();
             PreparedStatement ps = con.prepareStatement(SAVE_ORDER_PRODUCT_QUERY)) {
            ps.setLong(1, orderId);
            ps.setLong(2, item.getId());
            ps.setInt(3, item.getCount());
            ps.executeUpdate();
        }
    }

private void saveOrderItems(Long orderId, List<OrderItem> products, Connection con) throws SQLException {
    if (products == null || products.isEmpty()) {
        return;
    }
    try (PreparedStatement ps = con.prepareStatement(SAVE_ORDER_PRODUCT_QUERY)) {
        for (OrderItem item : products) {
            ps.setLong(1, orderId);
            ps.setLong(2, item.getId());
            ps.setInt(3, item.getCount());
            ps.addBatch();
        }
        //пакетный запрос для оптимизации, 1 обращение к бд вместо n
        ps.executeBatch();
    }
}

private void deleteOrderItems(Long orderId, Connection connection) throws SQLException {
    try (PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_PRODUCTS_QUERY)) {
        statement.setLong(1, orderId);
        statement.executeUpdate();
    }
}

private OrderItem toOrderItem(ResultSet rs) throws SQLException {
    return new OrderItem(
            rs.getLong("product_id"),
            rs.getString("product_name"),
            rs.getDouble("price"),
            rs.getInt("count")
    );
}

private Order toOrder(ResultSet rs) throws SQLException {
    return new Order(
            rs.getLong("order_id"),
            rs.getLong("user_id"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            Status.fromCode(rs.getString("status"))
    );
}
}
