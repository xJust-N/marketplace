package ru.itis.marketplace.models;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Entity<Long>{
    private Long id;
    private final Long userId;
    private String name;
    private String description;
    private List<Product> productList;

    public Shop(Long id, Long userId, String name, String description) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        productList = new ArrayList<>();
    }

    public Shop(Long userId, String name, String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        productList = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }
}
