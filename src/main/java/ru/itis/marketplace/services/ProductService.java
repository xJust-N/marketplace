package ru.itis.marketplace.services;

import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.Shop;

import java.util.List;

public interface ProductService extends Service<Long, Product>{

    Long getProductOwnerId(Product product);

    void updateProduct(Product product, String name, String price, String description, String stockQuantity, String active);

    Long createProduct(Shop shop, String name, String price, String description);

    List<Product> getAllActive(int limit, long offset);

    Long getActiveProductsTotalPages(int maxProductsAtPage);
}
