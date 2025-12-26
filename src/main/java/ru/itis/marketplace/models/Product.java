package ru.itis.marketplace.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Product implements Entity<Long> {
    private Long id;
    private final Long shopId;
    private String name;
    private double price;
    private String description;
    private Integer stockQuantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    private Shop shop = null;
    private List<Review> reviews;

    public Product(Long id, Long shopId, String name, double price,
                   String description, Integer stockQuantity,
                   LocalDateTime createdAt, LocalDateTime updatedAt, boolean active) {

        this.id = id;
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        reviews = new ArrayList<>();
    }

    public Product(Long shopId, String name, double price, String description) {
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.description = description;
        stockQuantity = 0;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        active = true;
        reviews = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
