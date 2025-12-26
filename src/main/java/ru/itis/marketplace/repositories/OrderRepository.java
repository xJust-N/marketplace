package ru.itis.marketplace.repositories;

import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.OrderItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderRepository<ID, T> extends Repository<ID, T> {
    Optional<T> findCartByUserId(Long userId) throws SQLException;

    void deleteCartByUserId(Long userId) throws SQLException;

    List<Order> getByUserId(Long userId) throws SQLException;

    List<OrderItem> getOrderItemsByOrderId(ID id) throws SQLException;

    void addOrderItem(ID id, OrderItem orderItem) throws SQLException;
}
