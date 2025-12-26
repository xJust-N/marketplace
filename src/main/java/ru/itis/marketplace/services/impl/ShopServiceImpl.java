package ru.itis.marketplace.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.models.enums.UserRole;
import ru.itis.marketplace.repositories.ProductRepository;
import ru.itis.marketplace.repositories.ShopRepository;
import ru.itis.marketplace.repositories.UserRepository;
import ru.itis.marketplace.services.ShopService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.itis.marketplace.utils.Validator.*;

public class ShopServiceImpl implements ShopService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ShopRepository<Long, Shop> shopRepository;
    private final ProductRepository<Long, Product> productRepository;
    private final UserRepository<Long, User> userRepository;

    private Long shopsCount = null;

    public ShopServiceImpl(ShopRepository<Long, Shop> shopRepository, ProductRepository<Long, Product> productRepository, UserRepository<Long, User> userRepository) {
        this.shopRepository = shopRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long createShop(User user, String name, String description) throws ValidationException {
        if(user.getShop() != null)
            throw new ValidationException("Shop already exists");

        logger.info("Creating shop {}", name);
        validateNotBlank(name, "shop name");
        validateNotBlank(description, "shop description");

        Shop shop = new Shop(
                user.getId(),
                escapeAndFormat(name),
                escapeAndFormat(description)
        );

        Long shopId;
        try {
            shopId = shopRepository.save(shop);
            shop.setId(shopId);
            logger.debug("Successfully created shop with id: {}", shopId);
        } catch (SQLException e) {
            logger.error("Error creating shop", e);
            throw new RuntimeException(e);
        }

        shopsCount = null;
        user.setShop(shop);
        if(user.getRole() == UserRole.CUSTOMER) {
            user.setRole(UserRole.CUSTOMER_AND_SELLER);
            try {
                userRepository.update(user);
                logger.debug("Updated user {} role", user.getId());
            } catch (SQLException e) {
                logger.error("Error updating user role", e);
                throw new RuntimeException(e);
            }
        }
        return shopId;
    }

    @Override
    public void update(Shop shop, String name, String description) throws ValidationException {
        logger.debug("Updating shop {}", shop.getId());

        validateNotBlank(name, "shop name");
        validateNotBlank(description, "shop description");

        shop.setName(escapeAndFormat(name));
        shop.setDescription(escapeAndFormat(description));

        try {
            shopRepository.update(shop);
            logger.debug("Successfully updated shop {}", shop.getId());
        } catch (SQLException e) {
            logger.error("Error updating shop {}", shop.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getTotalPages(int maxShopsAtPage) {
        Long totalShops = getTotalShopsCount();
        if (totalShops == 0) {
            return 1L;
        }
        long totalPages = totalShops / maxShopsAtPage;
        if (totalShops % maxShopsAtPage != 0) {
            totalPages++;
        }

        return totalPages;
    }

    @Override
    public List<Shop> getAll(int limit, long offset) {
        List<Shop> shops;
        try {
            logger.debug("Finding all shops by limit: {}, offset: {}", limit, offset);
            shops = shopRepository.getAll(limit, offset);
            if (shops == null) {
                shops = new ArrayList<>();
            }
            logger.debug("Successfully found {} shops", shops.size());
        } catch (SQLException e) {
            logger.error("Error finding all shops by limit: {}, offset: {}", limit, offset, e);
            throw new RuntimeException(e);
        }
        return shops;
    }

    @Override
    public Optional<Shop> findById(Long id) {
        Optional<Shop> shopOptional;
        try {
            logger.debug("Finding shop by id: {}", id);
            shopOptional = shopRepository.findById(id);
            if (shopOptional.isPresent()) {
                logger.debug("Successfully found shop by id: {}", id);
                shopOptional = Optional.of(
                        setProductsToShop(shopOptional.get())
                );
            } else {
                logger.debug("Shop not found by id: {}", id);
            }
        } catch (SQLException e) {
            logger.error("Error finding shop by id: {}", id, e);
            throw new RuntimeException(e);
        }
        return shopOptional;
    }

    @Override
    public void deleteById(Long id) {
        try {
            logger.info("Deleting shop by id: {}", id);
            shopRepository.deleteById(id);
            logger.info("Successfully deleted shop by id: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting shop by id: {}", id, e);
            throw new RuntimeException(e);
        }
        shopsCount = null;
    }

    private Long getTotalShopsCount() {
        if (shopsCount == null) {
            try {
                logger.debug("Finding total shops count");
                shopsCount = shopRepository.getTotalCount();
                logger.debug("Successfully found total shops count: {}", shopsCount);
            } catch (SQLException e) {
                logger.error("Error finding total shops count", e);
                throw new RuntimeException(e);
            }
        }
        return shopsCount;
    }
    private Shop setProductsToShop(Shop shop){
        try {
            logger.debug("Setting products to shop {} {}", shop.getId(), shop.getName());
            List<Product> products = productRepository.getProductsByShopId(shop.getId());
            if(products != null) {
                shop.setProductList(products);
                logger.debug("Successfully set products to shop {} {}", shop.getId(), shop.getName());
            }
            else{
                logger.debug("Products for shop not found");
            }
        } catch (SQLException e) {
            logger.error("Error setting products to shop {}", shop.getId(), e);
            throw new RuntimeException(e);
        }
        return shop;
    }
}