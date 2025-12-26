package ru.itis.marketplace.models;

import ru.itis.marketplace.models.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order implements Entity<Long> {
    private Long id;
    private final Long userId;
    private LocalDateTime createdAt;
    private Status status;
    private List<OrderItem> products;

    public Order(Long id, Long userId, LocalDateTime createdAt,
                 Status status) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
        products = new ArrayList<>();
    }

    public Order(Long userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.status = Status.SHOPPING_CART;
        products = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return products;
    }

    public void setOrderItems(List<OrderItem> products) {
        this.products = products;
    }

    public double getTotal(){
        double count = 0;
        for(OrderItem product : products){
            count += product.getPrice() * product.getCount();
        }
        return count;
    }
}
