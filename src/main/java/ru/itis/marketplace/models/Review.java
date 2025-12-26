package ru.itis.marketplace.models;

public class Review implements Entity<Long> {
    private Long id;
    private final long productId;
    private final long reviewerId;
    private short value;
    private String title;
    private String content;

    public Review(Long id, long productId, long reviewerId, short value, String title, String content) {
        this.id = id;
        this.productId = productId;
        this.reviewerId = reviewerId;
        this.value = value;
        this.title = title;
        this.content = content;
    }

    public Review(long productId, long reviewerId, short value, String title, String content) {
        this.productId = productId;
        this.reviewerId = reviewerId;
        this.value = value;
        this.title = title;
        this.content = content;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
