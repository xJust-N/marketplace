package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.repositories.ShopRepository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ShopJdbcRepository implements ShopRepository<Long, Shop> {

    private static final String SAVE_SHOP_QUERY = """
            insert into shops(user_id, shop_name, description)
            values (?, ?, ?)
            returning shop_id;
            """;
    private static final String FIND_SHOP_BY_ID_QUERY = """
            select * from shops where shop_id = ?;
            """;
    private static final String DELETE_SHOP_BY_ID_QUERY = """
            delete from shops where shop_id = ?;
            """;
    private static final String UPDATE_SHOP_QUERY = """
            update shops
            set shop_name = ?, description = ?
            where shop_id = ?;
            """;
    private static final String GET_SHOPS_PAGINATED_QUERY = """
            select * from shops
            order by shop_name
            limit ? offset ?;
            """;
    private static final String FIND_SHOP_BY_USER_ID_QUERY = """
            select * from shops
            where user_id = ?
            order by shop_name;
            """;
    private static final String GET_ALL_SHOPS_COUNT = """
            select count(shop_id) from shops;
            """;

    private final ConnectionHolder holder;
    private final BaseJdbcRepository<Long, Shop> baseShopRepository;

    public ShopJdbcRepository(ConnectionHolder holder) {
        this.holder = holder;
        this.baseShopRepository = new BaseJdbcRepository<>(holder, this::toShop, Long.class);
    }

    @Override
    public Optional<Shop> findById(Long id) throws SQLException {
        return baseShopRepository.find(FIND_SHOP_BY_ID_QUERY, id);
    }

    @Override
    public Long save(Shop shop) throws SQLException {
        List<Object> params = List.of(
                shop.getUserId(),
                shop.getName(),
                shop.getDescription()
        );
        return baseShopRepository.saveWithGeneratedKey(SAVE_SHOP_QUERY, params);
    }

    @Override
    public void update(Shop shop) throws SQLException {
        List<Object> params = List.of(
                shop.getName(),
                shop.getDescription(),
                shop.getId()
        );
        baseShopRepository.update(UPDATE_SHOP_QUERY, params);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        baseShopRepository.deleteById(DELETE_SHOP_BY_ID_QUERY, id);
    }

    @Override
    public List<Shop> getAll(int limit, long offset) throws SQLException {
        return baseShopRepository.getAll(GET_SHOPS_PAGINATED_QUERY, List.of(limit, offset));
    }

    public Long getTotalCount() throws SQLException {
        Long count = null;
        try (Connection con = holder.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ALL_SHOPS_COUNT)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    count = rs.getLong(1);
            }
        }
        return count;
    }

    @Override
    public Optional<Shop> findByUserId(Long userId) throws SQLException {
        return baseShopRepository.find(FIND_SHOP_BY_USER_ID_QUERY, userId);
    }

    private Shop toShop(ResultSet rs) throws SQLException {
        return new Shop(
                rs.getLong("shop_id"),
                rs.getLong("user_id"),
                rs.getString("shop_name"),
                rs.getString("description")
        );
    }
}