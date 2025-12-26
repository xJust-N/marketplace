package ru.itis.marketplace.repositories.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionHolder {
    Connection getConnection() throws SQLException;
}
