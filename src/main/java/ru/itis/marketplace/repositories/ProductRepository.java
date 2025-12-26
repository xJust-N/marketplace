package ru.itis.marketplace.repositories;

import ru.itis.marketplace.models.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductRepository<ID, T> extends Repository<ID, T> {

    List<T> getAllActive(long limit, long offset) throws SQLException;

    Long getProductOwnerId(Product product) throws SQLException;

    Long getActiveTotalCount() throws SQLException;

    List<T> getProductsByShopId(ID id) throws SQLException;
}