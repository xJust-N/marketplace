package ru.itis.marketplace.services;

import java.util.Optional;

public interface Service<ID, T> {

    Optional<T> findById(ID id);

    void deleteById(ID id);

}
