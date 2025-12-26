package ru.itis.marketplace.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.OrderItem;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.enums.Status;
import ru.itis.marketplace.repositories.OrderRepository;
import ru.itis.marketplace.services.OrderService;
import ru.itis.marketplace.utils.Validator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderRepository<Long, Order> orderRepository;
    
    public OrderServiceImpl(OrderRepository<Long, Order> orderRepository) {
        this.orderRepository = orderRepository;
        logger.info("OrderServiceImpl initialized");
    }

    @Override
    public void addToCart(Long userId, Product product, String quantityStr) {
        logger.debug("Adding product {} to cart for user {}", product.getId(), userId);
        int quantity = Validator.validateInt(quantityStr);
        if(quantity <= 0) {
            logger.warn("Invalid quantity {} for product {} and user {}", quantity, product.getId(), userId);
            throw new ValidationException("Quantity must be positive");
        }
        try {
            Order cart = getCartByUserId(userId);
            orderRepository.addOrderItem(cart.getId(), new OrderItem(product, quantity));
            logger.debug("Successfully added product {} to cart for user {}", product.getId(), userId);
        } catch (SQLException e) {
            logger.error("Error adding product {} to cart for user {}", product.getId(), userId, e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public Order getCartByUserId(Long userId) {
        logger.debug("Getting cart for user {}", userId);
        Optional<Order> cartOptional;
        Order cart;
        try {
            cartOptional = orderRepository.findCartByUserId(userId);
            if (cartOptional.isEmpty()) {
                logger.debug("Cart not found for user {}, creating new cart", userId);
                cart = new Order(userId);
                Long cartId = orderRepository.save(cart);
                cart.setId(cartId);
                logger.debug("Successfully created cart {} for user {}", cartId, userId);
            } else {
                cart = cartOptional.get();
                logger.debug("Found existing cart {} for user {}", cart.getId(), userId);
            }
            return setOrderItemsToOrder(cart);
        } catch (SQLException e) {
            logger.error("Error getting cart for user {}", userId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCartByUserId(Long userId) {
        logger.info("Deleting cart for user {}", userId);
        try {
            orderRepository.deleteCartByUserId(userId);
            logger.info("Successfully deleted cart for user {}", userId);
        } catch (SQLException e) {
            logger.error("Error deleting cart for user {}", userId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long createOrderFromCartByUserId(Long userId) {
        logger.info("Creating order from cart for user {}", userId);
        Order cart = getCartByUserId(userId);
        cart.setStatus(Status.PENDING);
        cart.setCreatedAt(LocalDateTime.now());
        try {
            orderRepository.update(cart);
            logger.info("Successfully created order {} from cart for user {}", cart.getId(), userId);
        } catch (SQLException e) {
            logger.error("Error creating order from cart for user {}", userId, e);
            throw new RuntimeException(e);
        }
        return cart.getId();
    }

    @Override
    public List<Order> getByUserId(Long userId) {
        logger.debug("Getting orders for user {}", userId);
        try {
            List<Order> orders = orderRepository.getByUserId(userId);
            logger.debug("Successfully found {} orders for user {}", orders.size(), userId);
            return orders;
        } catch (SQLException e) {
            logger.error("Error getting orders for user {}", userId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelOrder(Long orderId) {
        logger.info("Cancelling order {}", orderId);
        try {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setStatus(Status.CANCELLED);
                orderRepository.update(order);
                logger.info("Successfully cancelled order {}", orderId);
            } else {
                logger.warn("Order {} not found for cancellation", orderId);
            }
        } catch (SQLException e) {
            logger.error("Error cancelling order {}", orderId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        logger.debug("Finding order by id {}", orderId);
        Optional<Order> orderOpt;
        try {
            orderOpt = orderRepository.findById(orderId);
            if(orderOpt.isPresent()){
                logger.debug("Order {} found, setting order items", orderId);
                Order order = orderOpt.get();
                orderOpt = Optional.of(setOrderItemsToOrder(order));
                logger.debug("Successfully found order {} with items", orderId);
            } else {
                logger.debug("Order {} not found", orderId);
            }
        } catch (SQLException e) {
            logger.error("Error finding order by id {}", orderId, e);
            throw new RuntimeException(e);
        }
        return orderOpt;
    }

    private Order setOrderItemsToOrder(Order order) {
        logger.debug("Setting order items for order {}", order.getId());
        List<OrderItem> items;
        try {
           items = orderRepository.getOrderItemsByOrderId(order.getId());
           logger.debug("Successfully retrieved {} items for order {}", items.size(), order.getId());
        } catch (SQLException e) {
            logger.error("Error getting order items for order {}", order.getId(), e);
            throw new RuntimeException(e);
        }
        order.setOrderItems(items);
        return order;
    }

}
