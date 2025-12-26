package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.User;
import ru.itis.marketplace.repositories.UserRepository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;
import ru.itis.marketplace.models.enums.UserRole;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserJdbcRepository implements UserRepository<Long, User> {

    private static final String SAVE_USER_QUERY = """
            insert into users(login, password_hash, salt, role) values (?, ?, ?, ?)
            returning user_id;
            """;
    private static final String FIND_USER_BY_ID_QUERY = """
            select * from users where user_id = ?;
            """;
    private static final String DELETE_USER_BY_ID_QUERY = """
            delete from users where user_id = ?;
            """;
    private static final String UPDATE_USER_QUERY = """
            update users set login = ?, password_hash = ?, salt = ?, role = ? where user_id = ?;
            """;
    private static final String FIND_USER_BY_LOGIN_QUERY = """
            select * from users where login = ?;
            """;

    private final BaseJdbcRepository<Long, User> baseRepository;

    public UserJdbcRepository(ConnectionHolder holder) {
        this.baseRepository = new BaseJdbcRepository<>(holder, this::toUser, Long.class);
    }

    @Override
    public Optional<User> findByLogin(String username) throws SQLException {
        return baseRepository.find(FIND_USER_BY_LOGIN_QUERY, username);
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        return baseRepository.find(FIND_USER_BY_ID_QUERY, id);
    }

    @Override
    public Long save(User user) throws SQLException {
        List<Object> params = List.of(user.getLogin(), user.getPasswordHash(), user.getSalt(), user.getRole().getCode());
        return baseRepository.saveWithGeneratedKey(SAVE_USER_QUERY, params);
    }

    @Override
    public void update(User user) throws SQLException {
        List<Object> params = List.of(user.getLogin(), user.getPasswordHash(), user.getSalt(), user.getRole().getCode(), user.getId());
        baseRepository.update(UPDATE_USER_QUERY, params);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        baseRepository.deleteById(DELETE_USER_BY_ID_QUERY, id);
    }

    private User toUser(ResultSet rs) throws SQLException, IllegalArgumentException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("login"),
                rs.getString("password_hash"),
                rs.getString("salt"),
                UserRole.fromCode(rs.getString("role"))
        );
    }
}