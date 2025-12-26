package ru.itis.marketplace.listeners;

import jakarta.servlet.annotation.WebListener;
import ru.itis.marketplace.models.*;
import ru.itis.marketplace.repositories.*;
import ru.itis.marketplace.repositories.impl.*;
import ru.itis.marketplace.repositories.util.ConnectionHolder;
import ru.itis.marketplace.repositories.util.ConnectionHolderImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.itis.marketplace.services.*;
import ru.itis.marketplace.services.impl.*;
import ru.itis.marketplace.config.AppProperties;

import java.util.UUID;

@WebListener
public class InitListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {

        String url = AppProperties.getProperty("database.url");
        String username = AppProperties.getProperty("database.username");
        String password = AppProperties.getProperty("database.password");

        ConnectionHolder holder = new ConnectionHolderImpl(url, username, password);
        UserRepository<Long, User> userRepository = new UserJdbcRepository(holder);
        Repository<UUID, Session> sessionRepository = new SessionJdbcRepository(holder);

        ProductRepository<Long, Product> productRepository = new ProductJdbcRepository(holder);
        ReviewRepository<Long, Review> reviewRepository = new ReviewJdbcRepository(holder);
        ShopRepository<Long, Shop> shopRepository = new ShopJdbcRepository(holder);
        OrderRepository<Long, Order> orderRepository = new OrderJdbcRepository(holder);

        ProductService productService = new ProductServiceImpl(productRepository, reviewRepository, shopRepository);
        ReviewService reviewService = new ReviewServiceImpl(reviewRepository);
        ShopService shopService = new ShopServiceImpl(shopRepository, productRepository, userRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository);

        SecurityService<UUID> securityService = new SecurityServiceImpl(userRepository, shopRepository, sessionRepository);
        sce.getServletContext().setAttribute("securityService", securityService);
        sce.getServletContext().setAttribute("productService", productService);
        sce.getServletContext().setAttribute("reviewService", reviewService);
        sce.getServletContext().setAttribute("shopService", shopService);
        sce.getServletContext().setAttribute("orderService", orderService);
    }

}

