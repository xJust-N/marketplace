package ru.itis.marketplace.repositories.impl;

import ru.itis.marketplace.models.Review;
import ru.itis.marketplace.repositories.ReviewRepository;
import ru.itis.marketplace.repositories.util.ConnectionHolder;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ReviewJdbcRepository implements ReviewRepository<Long, Review> {

    private static final String SAVE_REVIEW_QUERY = """
            insert into reviews(product_id, buyer_id, value, title, content)
            values (?, ?, ?, ?, ?)
            returning review_id;
            """;
    private static final String FIND_REVIEW_BY_ID_QUERY = """
            select * from reviews where review_id = ?;
            """;
    private static final String DELETE_REVIEW_BY_ID_QUERY = """
            delete from reviews where review_id = ?;
            """;
    private static final String UPDATE_REVIEW_QUERY = """
            update reviews
            set value = ?, title = ?, content = ?
            where review_id = ?;
            """;
    private static final String GET_REVIEWS_BY_PRODUCT_ID_QUERY = """
            select * from reviews
            where product_id = ?
            order by review_id desc;
            """;
    private static final String FIND_REVIEW_BY_PRODUCT_ID_USER_ID =
            "select * from reviews where product_id = ? and buyer_id = ?";

    private final BaseJdbcRepository<Long, Review> baseReviewRepository;

    public ReviewJdbcRepository(ConnectionHolder holder) {
        this.baseReviewRepository = new BaseJdbcRepository<>(holder, this::toReview, Long.class);
    }

    @Override
    public List<Review> getByProductId(Long productId) throws SQLException {
        return baseReviewRepository.getAll(GET_REVIEWS_BY_PRODUCT_ID_QUERY, List.of(productId));
    }

    @Override
    public Optional<Review> findByProductAndUserId(long productId, Long reviewerId) throws SQLException {
        return baseReviewRepository.find(FIND_REVIEW_BY_PRODUCT_ID_USER_ID, productId, reviewerId);
    }

    @Override
    public Optional<Review> findById(Long id) throws SQLException {
        return baseReviewRepository.find(FIND_REVIEW_BY_ID_QUERY, id);
    }

    @Override
    public Long save(Review review) throws SQLException {
        List<Object> params = List.of(
                review.getProductId(),
                review.getReviewerId(),
                review.getValue(),
                review.getTitle(),
                review.getContent()
        );
        return baseReviewRepository.saveWithGeneratedKey(SAVE_REVIEW_QUERY, params);
    }

    @Override
    public void update(Review review) throws SQLException {
        List<Object> params = List.of(
                review.getValue(),
                review.getTitle(),
                review.getContent(),
                review.getId()
        );
        baseReviewRepository.update(UPDATE_REVIEW_QUERY, params);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        baseReviewRepository.deleteById(DELETE_REVIEW_BY_ID_QUERY, id);
    }

    private Review toReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getLong("review_id"),
                rs.getLong("product_id"),
                rs.getLong("buyer_id"),
                rs.getShort("value"),
                rs.getString("title"),
                rs.getString("content")
        );
    }
}