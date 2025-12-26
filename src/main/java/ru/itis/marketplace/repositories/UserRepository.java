package ru.itis.marketplace.repositories;

import ru.itis.marketplace.models.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository<T, ID> extends Repository<T, ID> {

    Optional<User> findByLogin(String username) throws SQLException;
}
