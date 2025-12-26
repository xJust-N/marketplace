package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.repositories.ProductRepository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ProductJdbcRepository implements ProductRepository<Long, Product> {

    private static final String SAVE_PRODUCT_QUERY = """
            insert into products(shop_id, product_name, price, description, stock_quantity, created_at, updated_at, active)
            values (?, ?, ?, ?, ?, ?, ?, ?)
            returning product_id;
            """;
    private static final String FIND_PRODUCT_BY_ID_QUERY = """
            select * from products where product_id = ?;
            """;
    private static final String DELETE_PRODUCT_BY_ID_QUERY = """
            delete from products where product_id = ?;
            """;
    private static final String UPDATE_PRODUCT_QUERY = """
            update products
            set product_name = ?, price = ?, description = ?, stock_quantity = ?, updated_at = ?, active = ?
            where product_id = ?;
            """;

    private static final String GET_ACTIVE_PRODUCTS_PAGINATED_QUERY = """
            select * from products
            where active = true
            order by created_at desc
            limit ? offset ?;
            """;
    private static final String GET_OWNER_BY_SHOP_ID = """
            select user_id
            from shops
            where shop_id = ?;
            """;
    private static final String GET_ALL_ACTIVE_PRODUCTS_COUNT = """
            select count(product_id)
            from products
            where active = true;
            """;
    private static final String GET_ALL_PRODUCTS_BY_SHOP_ID = """
            select * from products where shop_id = ?;
            """;

    private final BaseJdbcRepository<Long, Product> productBaseRepository;
    private final ConnectionHolder holder;

    public ProductJdbcRepository(ConnectionHolder holder) {
        this.holder = holder;
        this.productBaseRepository = new BaseJdbcRepository<>(holder, this::toProduct, Long.class);
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        return productBaseRepository.find(FIND_PRODUCT_BY_ID_QUERY, id);
    }

    @Override
    public Long save(Product product) throws SQLException {
        List<Object> params = List.of(
                product.getShopId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStockQuantity(),
                Timestamp.valueOf(product.getCreatedAt()),
                Timestamp.valueOf(product.getUpdatedAt()),
                product.isActive()
        );
        return productBaseRepository.saveWithGeneratedKey(SAVE_PRODUCT_QUERY, params);
    }

    @Override
    public void update(Product product) throws SQLException {
        List<Object> params = List.of(
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStockQuantity(),
                Timestamp.valueOf(product.getUpdatedAt()),
                product.isActive(),
                product.getId()
        );
        productBaseRepository.update(UPDATE_PRODUCT_QUERY, params);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        productBaseRepository.deleteById(DELETE_PRODUCT_BY_ID_QUERY, id);
    }

    @Override
    public List<Product> getAllActive(long limit, long offset) throws SQLException {
        return productBaseRepository.getAll(GET_ACTIVE_PRODUCTS_PAGINATED_QUERY, List.of(limit, offset));
    }

    @Override
    public Long getProductOwnerId(Product product) throws SQLException {
        Long shopId = product.getShopId();
        try(Connection con = holder.getConnection();
            PreparedStatement ps = con.prepareStatement(GET_OWNER_BY_SHOP_ID)){
            ps.setLong(1, shopId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    return rs.getLong("user_id");
                } else {
                    throw new SQLException("Shop not found for product: " + product.getId());
                }
            }
        }
    }

    @Override
    public Long getActiveTotalCount() throws SQLException {
        Long count = null;
        try(Connection con = holder.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(GET_ALL_ACTIVE_PRODUCTS_COUNT)){
            try(ResultSet rs = preparedStatement.executeQuery()){
                if(rs.next())
                    count = rs.getLong(1);
            }
        }
        return count;
    }

    @Override
    public List<Product> getProductsByShopId(Long shopId) throws SQLException {
        return productBaseRepository.getAll(GET_ALL_PRODUCTS_BY_SHOP_ID, List.of(shopId));
    }


    private Product toProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getLong("product_id"),
                rs.getLong("shop_id"),
                rs.getString("product_name"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getInt("stock_quantity"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getBoolean("active")
        );
    }
}