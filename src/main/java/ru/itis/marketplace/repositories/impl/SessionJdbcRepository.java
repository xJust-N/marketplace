package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.Session;
import ru.itis.marketplace.repositories.Repository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SessionJdbcRepository implements Repository<UUID, Session> {

    private static final String FIND_SESSION_BY_ID_QUERY = """
            select * from sessions where session_id = ?;
            """;
    private static final String DELETE_SESSION_BY_ID_QUERY = """
            delete from sessions where session_id = ?;
            """;
    private static final String SAVE_SESSION_QUERY = """
            insert into sessions (session_id, user_id, expire_at) values (?, ?, ?);
            """;
    private static final String UPDATE_SESSION_QUERY = """
            update sessions set user_id = ?, expire_at = ? where session_id = ?;
            """;

    private final BaseJdbcRepository<String, Session> baseRepository;

    public SessionJdbcRepository(ConnectionHolder holder) {
        this.baseRepository = new BaseJdbcRepository<>(holder, this::toSession, String.class);
    }

    @Override
    public Optional<Session> findById(UUID id) throws SQLException {
        return baseRepository.find(FIND_SESSION_BY_ID_QUERY, id.toString());
    }

    @Override
    public UUID save(Session session) throws SQLException {
        List<Object> params = List.of(
                session.getId().toString(),
                session.getUserId(),
                session.getExpireAt()
        );
        baseRepository.saveWithoutGeneratedKey(SAVE_SESSION_QUERY, params);
        return session.getId();
    }

    @Override
    public void update(Session session) throws SQLException {
        List<Object> params = List.of(
                session.getUserId(),
                session.getExpireAt(),
                session.getId().toString()
        );
        baseRepository.update(UPDATE_SESSION_QUERY, params);
    }

    @Override
    public void deleteById(UUID id) throws SQLException {
        baseRepository.deleteById(DELETE_SESSION_BY_ID_QUERY, id.toString());
    }

    private Session toSession(ResultSet rs) throws SQLException {
        return new Session(
                UUID.fromString(rs.getString("session_id")),
                rs.getLong("user_id"),
                rs.getTimestamp("expire_at").toLocalDateTime()
        );
    }
}