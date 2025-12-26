package ru.itis.marketplace.models;

import ru.itis.marketplace.models.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class User implements Entity<Long> {
    private Long id;
    private final String login;
    private final String passwordHash;
    private final String salt;
    private UserRole role;
    private List<Order> orders;
    private Shop shop;

    public User(Long id, String login, String passwordHash,
                String salt, UserRole role) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        orders = new ArrayList<>();
        shop = null;
    }

    public User(String login, String passwordHash, String salt, UserRole role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        orders = new ArrayList<>();
        shop = null;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
