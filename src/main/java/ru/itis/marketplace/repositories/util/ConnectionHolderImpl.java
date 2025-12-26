package ru.itis.marketplace.repositories.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHolderImpl implements ConnectionHolder {
    private final String url;
    private final String login;
    private final String password;

    public ConnectionHolderImpl(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
        init();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    private void init(){
        try {
            Class.forName("org.postgresql.Driver"); //Без этого - ошибка нахождения драйвера
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver load failed", e);
        }
    }
}
