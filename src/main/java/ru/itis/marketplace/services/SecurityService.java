package ru.itis.marketplace.services;

import ru.itis.marketplace.models.User;

import java.util.Optional;

public interface SecurityService<ID> {
    ID registerUser(String login, String password, String passwordRepeat);

    ID loginUser(String login, String password);

    Optional<User> findUserById(Long userId);

    Optional<User> findUserBySessionId(ID sessionId);

    void deleteSessionById(ID sessionId);
}
