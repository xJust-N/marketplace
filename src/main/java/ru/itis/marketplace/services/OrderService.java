package ru.itis.marketplace.services;

import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.Product;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void addToCart(Long userId, Product product, String quantityStr);

    Order getCartByUserId(Long userId);

    void deleteCartByUserId(Long userId);

    Long createOrderFromCartByUserId(Long userId);

    List<Order> getByUserId(Long userId);

    void cancelOrder(Long orderId);

    Optional<Order> findById(Long orderId);
}
