package ru.itis.marketplace.services;

import ru.itis.marketplace.models.Review;

public interface ReviewService extends Service<Long, Review>{
    Long createReview(Long userId, String id, String value, String trim, String content);

    void updateReview(Review review, String value, String trim, String content);

    boolean isReviewExists(Long productId, Long userId);
}
