package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.repositories.util.ConnectionHolder;
import ru.itis.marketplace.repositories.util.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Базовый jdbc репозиторий, другие репозитории делегируют вызов методов ему
class BaseJdbcRepository<ID, T> {
    private final ConnectionHolder connectionHolder;
    private final RowMapper<T> mapper;
    private final Class<ID> idClass;

    BaseJdbcRepository(ConnectionHolder connectionHolder,
                       RowMapper<T> mapper, Class<ID> idClass) {
        this.connectionHolder = connectionHolder;
        this.mapper = mapper;
        this.idClass = idClass;
    }

    Optional<T> find(String sql, Object... keys) throws SQLException {
        try (Connection con = connectionHolder.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < keys.length; i++) {
                ps.setObject(i + 1, keys[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapper.mapRow(rs));
            }
        }
        return Optional.empty();
    }

    ID saveWithGeneratedKey(String sql, List<Object> params) throws SQLException {
        try (Connection con = connectionHolder.getConnection()) {
            return saveWithGeneratedKey(sql, params, con);
        }
    }

    ID saveWithGeneratedKey(String sql, List<Object> params, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getObject(1, idClass);
            }
        }
        return null;
    }

    void saveWithoutGeneratedKey(String sql, List<Object> params) throws SQLException {
        try (Connection con = connectionHolder.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
        }
    }

    void update(String sql, List<Object> params) throws SQLException {
        try (Connection con = connectionHolder.getConnection()) {
            update(sql, params, con);
        }
    }

    void update(String sql, List<Object> params, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
        }
    }

    List<T> getAll(String sql, List<Object> params) throws SQLException {
        List<T> list = new ArrayList<>();
        try (Connection con = connectionHolder.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.mapRow(rs));
                }
            }
        }
        return list;
    }

    void deleteById(String sql, ID id) throws SQLException {
        try (Connection con = connectionHolder.getConnection()) {
            deleteById(sql, id, con);
        }
    }

    void deleteById(String sql, ID id, Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }
}
