package ru.itis.marketplace.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.config.AppProperties;
import ru.itis.marketplace.exceptions.AuthenticationException;
import ru.itis.marketplace.models.Session;
import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.models.enums.UserRole;
import ru.itis.marketplace.repositories.Repository;
import ru.itis.marketplace.repositories.ShopRepository;
import ru.itis.marketplace.repositories.UserRepository;
import ru.itis.marketplace.services.SecurityService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static ru.itis.marketplace.utils.Validator.containsOnlyLettersAndDigits;

public class SecurityServiceImpl implements SecurityService<UUID> {

    private static final int MIN_PASSWORD_LENGTH =
            Integer.parseInt(AppProperties.getProperty("security.password-min-length", "8"));

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository<Long, User> userRepository;
    private final ShopRepository<Long, Shop> shopRepository;
    private final Repository<UUID, Session> sessionRepository;
    private final Base64.Encoder base64Encoder;
    private final Duration sessionDuration;

    public SecurityServiceImpl(UserRepository<Long, User> userRepository,
                               ShopRepository<Long, Shop> shopRepository, Repository<UUID, Session> sessionRepository) {
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.sessionRepository = sessionRepository;
        this.base64Encoder = Base64.getEncoder();
        int duration = Integer.parseInt(AppProperties.getProperty("security.session-timeout-minutes"));
        this.sessionDuration = Duration.ofMinutes(duration);
        logger.info("SecurityServiceImpl created with session duration: {} minutes", duration);
    }

    @Override
    public UUID registerUser(String login, String password, String passwordRepeat) throws AuthenticationException {
        logger.info("Registering user: {}", login);
        if (!password.equals(passwordRepeat)) {
            logger.warn("Password mismatch for user: {}", login);
            throw new AuthenticationException("Passwords doesnt match");
        }
        checkLogin(login);
        checkPassword(password);

        String salt = UUID.randomUUID().toString();
        String saltedPassword = password + salt;
        String passwordHash = getPasswordHash(saltedPassword);
        User user = new User(login, passwordHash, salt, UserRole.CUSTOMER);
        try {
            Long userId = userRepository.save(user);
            Session session = new Session(userId, LocalDateTime.now().plus(sessionDuration));
            UUID sessionId = sessionRepository.save(session);
            logger.info("User registered successfully: {} with session: {}", login, sessionId);
            return sessionId;
        } catch (SQLException e) {
            logger.error("Failed to register user: {}", login, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID loginUser(String email, String password) throws AuthenticationException {
        logger.info("User login attempt: {}", email);
        User user;
        try {
            user = userRepository.findByLogin(email).orElseThrow(SQLException::new);
        } catch (SQLException e) {
            logger.warn("Login not found: {}", email);
            throw new AuthenticationException("Login not found");
        }
        String salt = user.getSalt();
        String saltedPassword = password + salt;
        String passwordHash = getPasswordHash(saltedPassword);
        if (!passwordHash.equals(user.getPasswordHash())) {
            logger.warn("Invalid password for user: {}", email);
            throw new AuthenticationException("Invalid password");
        }
        try {
            Session session = new Session(user.getId(), LocalDateTime.now().plus(sessionDuration));
            UUID sessionId = sessionRepository.save(session);
            logger.info("User logged in successfully: {} with session: {}", email, sessionId);
            return sessionId;
        } catch (SQLException e) {
            logger.error("Failed to create session for user: {}", email, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        logger.debug("Finding user by id: {}", userId);
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                logger.debug("User found with id: {}", userId);
                user = Optional.of(setShopToUserIfNeeded(user.get()));
            } else {
                logger.debug("User not found with id: {}", userId);
            }
            return user;
        } catch (SQLException e) {
            logger.error("Failed to find user by id: {}", userId, e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<User> findUserBySessionId(UUID sessionId) {
        logger.debug("Finding user by session: {}", sessionId);
        Session session;
        try {
            session = sessionRepository.findById(sessionId).orElseThrow(SQLException::new);
            if (session.getExpireAt().isBefore(LocalDateTime.now())) {
                logger.warn("Session expired: {}", sessionId);
                deleteSessionById(sessionId);
                throw new AuthenticationException("Session expired");
            }
            Optional<User> userOptional = userRepository.findById(session.getUserId());
            if (userOptional.isPresent()) {
                logger.debug("User found for session: {}", sessionId);
                return Optional.of( setShopToUserIfNeeded(userOptional.get()));
            } else {
                logger.warn("User not found for session: {}", sessionId);
            }
            return userOptional;
        } catch (SQLException e) {
            logger.error("Failed to find user by session: {}", sessionId, e);
            throw new AuthenticationException(e);
        }
    }

    @Override
    public void deleteSessionById(UUID sessionId) {
        try {
            logger.debug("Deleting session by id: {}", sessionId);
            sessionRepository.deleteById(sessionId);
        } catch (SQLException e) {
            logger.error("Failed to delete session by id: {}", sessionId, e);
            throw new RuntimeException(e);
        }
    }

    private User setShopToUserIfNeeded(User user) throws SQLException {
        if(!user.getRole().isSeller()){
            logger.debug("User {} with role {} doesnt need shop, skip set shop", user.getId(), user.getRole());
            return user;
        }
        logger.debug("Setting shop to: {}", user.getId());
        Optional<Shop> shopOpt = shopRepository.findByUserId(user.getId());
        if(shopOpt.isPresent()){
            user.setShop(shopOpt.get());
            logger.debug("Successfully found and set shop {} for user {}", user.getShop().getId(), user.getId());
        }
        else{
            logger.debug("Shop for user {} not found", user.getId());
        }
        return user;
    }

    private String getPasswordHash(String saltedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return base64Encoder.encodeToString(passwordHashBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA-256 algorithm not found", e);
            throw new RuntimeException(e);
        }
    }

    private void checkLogin(String login) throws AuthenticationException {
        if (!containsOnlyLettersAndDigits(login)) {
            logger.warn("Invalid characters in login: {}", login);
            throw new AuthenticationException("Login contains invalid characters");
        }
        try {
            if(userRepository.findByLogin(login).isPresent()){
                logger.warn("Login already exists: {}", login);
                throw new AuthenticationException("Login must be unique");
            }
        } catch (SQLException e) {
            logger.error("Failed to validate login uniqueness: {}", login, e);
            throw new RuntimeException(e);
        }
    }

    private void checkPassword(String password) throws AuthenticationException {
        if(password.trim().length() < MIN_PASSWORD_LENGTH) {
            logger.warn("Password too short: {} characters", password.length());
            throw new AuthenticationException((
                    "Password too short, should be at least %d characters").formatted(MIN_PASSWORD_LENGTH));
        }
        if(!containsOnlyLettersAndDigits(password)) {
            logger.warn("Invalid characters in password");
            throw new AuthenticationException("Password contains invalid characters, only letters, digits are allowed");
        }
    }
}