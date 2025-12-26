package ru.itis.marketplace.repositories;

import ru.itis.marketplace.models.Shop;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ShopRepository<ID, T>  extends Repository<ID, T>{
    List<T> getAll(int limit, long offset) throws SQLException;

    ID getTotalCount()throws SQLException;

    Optional<Shop> findByUserId(ID userId) throws SQLException;
}
