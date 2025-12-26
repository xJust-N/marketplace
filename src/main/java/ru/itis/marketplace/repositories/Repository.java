package ru.itis.marketplace.repositories;

import java.sql.SQLException;
import java.util.Optional;

public interface Repository<K, T> {

    Optional<T> findById(K id) throws SQLException;

    K save(T t) throws SQLException;

    void update(T t) throws SQLException;

    void deleteById(K id) throws SQLException;

}

