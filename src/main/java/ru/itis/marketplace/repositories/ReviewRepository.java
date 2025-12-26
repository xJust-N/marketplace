package ru.itis.marketplace.repositories;

import ru.itis.marketplace.models.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository<ID, T> extends Repository<ID, T> {

    List<T> getByProductId(Long id) throws SQLException;

    Optional<Review> findByProductAndUserId(long productId, Long userId)throws SQLException;
}
