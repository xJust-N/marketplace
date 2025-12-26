package ru.itis.marketplace.services;

import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.models.User;

import java.util.List;

public interface ShopService extends Service<Long, Shop>{

    Long createShop(User userId, String name, String description);

    void update(Shop shop, String name, String description);

    Long getTotalPages(int limit);

    List<Shop> getAll(int maxShopsAtPage, long l);
}
