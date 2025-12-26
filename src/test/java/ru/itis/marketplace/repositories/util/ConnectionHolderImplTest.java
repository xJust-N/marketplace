package ru.itis.marketplace.repositories.util;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionHolderImplTest {

    private ConnectionHolder holder;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        holder = new ConnectionHolderImpl("jdbc:postgresql://localhost:5432/marketplace", "postgres", "1234");
    }

    @org.junit.jupiter.api.Test
    void getConnection() throws SQLException {
        assertNotNull(holder.getConnection());
    }
}