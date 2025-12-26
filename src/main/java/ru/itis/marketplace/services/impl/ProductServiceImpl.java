package ru.itis.marketplace.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.Review;
import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.repositories.ProductRepository;
import ru.itis.marketplace.repositories.Repository;
import ru.itis.marketplace.repositories.ReviewRepository;
import ru.itis.marketplace.services.ProductService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.itis.marketplace.utils.Validator.*;

public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository<Long, Product> productRepository;
    private final ReviewRepository<Long, Review> reviewRepository;
    private final Repository<Long, Shop> shopRepository;

    private Long activeProductsCount = null;

    public ProductServiceImpl(ProductRepository<Long, Product> productRepository,
                              ReviewRepository<Long, Review> reviewRepository, Repository<Long, Shop> shopRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.shopRepository = shopRepository;
    }


    @Override
    public Long getProductOwnerId(Product product) {
        Long ownerId;
        try {
            logger.debug("Getting product owner id for product {}", product.getId());
            ownerId = productRepository.getProductOwnerId(product);
            logger.debug("Successfully retrieved product owner id for product {}", product.getId());
        } catch (SQLException e) {
            logger.error("Error retrieving product owner id for product {}", product.getId(), e);
            throw new RuntimeException(e);
        }
        return ownerId;
    }

    @Override
    public void updateProduct(Product product, String name, String price,
                              String description, String stockQuantity, String active) throws ValidationException {
        logger.debug("Updating product");
        validateNotBlank(name, "product name");
        double priceDouble = validateDouble(price);
        Integer stockQuantityInteger = null;
        if (stockQuantity != null) {
            stockQuantityInteger = validateInt(stockQuantity);
        }
        boolean isActive = active != null && (
                active.equalsIgnoreCase("on")
                || Boolean.parseBoolean(active)
        );

        product.setName(escapeAndFormat(name));
        product.setDescription(escapeAndFormat(description));
        product.setPrice(priceDouble);
        product.setStockQuantity(stockQuantityInteger);
        product.setUpdatedAt(LocalDateTime.now());
        product.setActive(isActive);

        try {
            productRepository.update(product);
            logger.debug("Successfully updated product");
        } catch (SQLException e) {
            logger.error("Error updating product", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Long createProduct(Shop shop, String name, String price, String description) throws ValidationException {
        logger.info("Creating product {}", name);
        validateNotBlank(name, "product name");
        double priceDouble = validateDouble(price);
        Product product = new Product(shop.getId(), escapeAndFormat(name),
                priceDouble, escapeAndFormat(description));
        product.setShop(shop);
        shop.addProduct(product);

        Long productId;
        try {
            productId = productRepository.save(product);
            product.setId(productId);
            logger.debug("Successfully created product");
        } catch (SQLException e) {
            logger.error("Error creating product", e);
            throw new RuntimeException(e);
        }
        activeProductsCount = null;
        return productId;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> productOptional;
        try {
            productOptional = productRepository.findById(id);
        } catch (SQLException e) {
            logger.error("Error finding product by id {}", id, e);
            throw new RuntimeException(e);
        }
        if(productOptional.isEmpty())
            return Optional.empty();
        Product product = productOptional.get();
        setShopToProduct(product);
        setReviewsToProduct(product);
        return Optional.of(product);
    }

    @Override
    public void deleteById(Long id) {
        try {
            logger.info("Deleting product by id: {}", id);
            productRepository.deleteById(id);
            logger.info("Successfully deleted product by id: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting product by id: {}", id, e);
            throw new RuntimeException(e);
        }
        activeProductsCount = null;

    }

    @Override
    public List<Product> getAllActive(int limit, long offset) {
        List<Product> products;
        try {
            logger.debug("Finding all active products by limit: {}, offset {}", limit, offset);
            products = productRepository.getAllActive(limit, offset);
            if(products == null)
                products = new ArrayList<>();
        } catch (SQLException e) {
            logger.error("Error finding all active products by limit: {}, offset: {}", limit, offset, e);
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Long getActiveProductsTotalPages(int maxProductsAtPage) {
        long activeProducts = getTotalActiveProductsCount() / maxProductsAtPage;
        if(activeProducts % maxProductsAtPage != 0)
            activeProducts++;
        return activeProducts;
    }

    private void setShopToProduct(Product product) {
        Long id = product.getShopId();
        Optional<Shop> shopOpt;
        try {
            logger.debug("Find product's shop by id: {}", id);
            shopOpt = shopRepository.findById(product.getShopId());
            logger.debug("Successfully find shop by id: {}", id);
        } catch (SQLException e) {
            logger.error("Error finding shop by id: {}", id, e);
            throw new RuntimeException(e);
        }
        shopOpt.ifPresent(product::setShop);
    }

    private void setReviewsToProduct(Product product) {
        List<Review> reviews;
        try{
            logger.debug("Finding reviews for product {}", product.getId());
            reviews = reviewRepository.getByProductId(product.getId());
            logger.debug("Successfully find reviews for product {}", product.getId());
        } catch (SQLException e){
            logger.error("Error finding reviews for product {}", product.getId(), e);
            throw new RuntimeException(e);
        }
        if(reviews == null)
            reviews = new ArrayList<>();
        product.setReviews(reviews);
    }

    private Long getTotalActiveProductsCount(){
        if(activeProductsCount == null) {
            try {
                logger.debug("Finding total active products count");
                activeProductsCount = productRepository.getActiveTotalCount();
                logger.debug("Successfully finding total active products count");
            } catch (SQLException e) {
                logger.error("Error finding total products count", e);
                throw new RuntimeException(e);
            }
        }
        return activeProductsCount;
    }

}
