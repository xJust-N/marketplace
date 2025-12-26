package ru.itis.marketplace.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Review;
import ru.itis.marketplace.repositories.ReviewRepository;
import ru.itis.marketplace.services.ReviewService;

import java.sql.SQLException;
import java.util.Optional;

import static ru.itis.marketplace.utils.Validator.*;

public class ReviewServiceImpl implements ReviewService {

    private static final int MIN_REVIEW_VALUE = 1;
    private static final int MAX_REVIEW_VALUE = 5;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewRepository<Long,Review> reviewRepository;

    public ReviewServiceImpl(ReviewRepository<Long, Review> reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Long createReview(Long userId, String productId, String value, String title, String content) {
        long productIdL = validateLong(productId);
        if(isReviewExists(productIdL, userId))
            throw new ValidationException("Review already exists");

        int valueInt = validateInt(value);
        if(valueInt < MIN_REVIEW_VALUE || valueInt > MAX_REVIEW_VALUE)
            throw new ValidationException("Value must be between %s and %s".formatted(MIN_REVIEW_VALUE, MAX_REVIEW_VALUE));
        validateNotBlank(title, "title");
        String titleEscaped = escapeAndFormat(title);
        String contentEscaped = escapeAndFormat(content);
        logger.info("Creating review for product {} by user {}", productIdL, productId);
        Review review = new Review(productIdL, userId, (short) valueInt, titleEscaped, contentEscaped);
        Long reviewId;
        try {
            reviewId = reviewRepository.save(review);
            logger.info("Successfully created review {}", reviewId);
        } catch (SQLException e) {
            logger.error("Error creating review", e);
            throw new RuntimeException(e);
        }
        return reviewId;
    }

    @Override
    public void updateReview(Review review, String value, String title, String content) {
        validateNotBlank(title, "title");
        int valueInt = validateInt(value);
        if(valueInt < MIN_REVIEW_VALUE || valueInt > MAX_REVIEW_VALUE)
            throw new ValidationException("Value must be between %s and %s".formatted(MIN_REVIEW_VALUE, MAX_REVIEW_VALUE));
        String titleEscaped = escapeAndFormat(title);
        String contentEscaped = escapeAndFormat(content);
        logger.info("Updating review {}", review.getId());
        review.setValue((short) valueInt);
        review.setTitle(titleEscaped);
        review.setContent(contentEscaped);
        try {
            reviewRepository.update(review);
            logger.info("Successfully updated review {}", review.getId());
        } catch (SQLException e) {
            logger.error("Error updating review", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Review> findById(Long aLong) {
        try {
            return reviewRepository.findById(aLong);
        } catch (SQLException e) {
            logger.error("Error finding review {}",aLong, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        logger.info("Deleting review {}", aLong);
        try{
            reviewRepository.deleteById(aLong);
            logger.info("Successfully deleted review {}", aLong);
        } catch (SQLException e) {
            logger.error("Error deleting review", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReviewExists(Long productId, Long userId){
        logger.debug("Checking if review exists for product {} and user {}", productId, userId);
        try {
            if (reviewRepository.findByProductAndUserId(productId, userId).isPresent()) {
                logger.debug("Review already exists for product {} and user {}", productId, userId);
                return true;
            }
            logger.debug("Review does not exist for product {} and user {}", productId, userId);
        } catch (SQLException e) {
            logger.error("Error checking if review exists for product {} and user {}", productId, userId, e);
            throw new RuntimeException(e);
        }
        return false;
    }
}
