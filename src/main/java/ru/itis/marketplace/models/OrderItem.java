package ru.itis.marketplace.models;

public class OrderItem implements Entity<Long>{
    private Long productId;
    private final String productName;
    private double price;
    private int count;

    public OrderItem(Long productId, String productName, double price, int count) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.count = count;
    }

    public OrderItem(Product product, int quantity) {
        this(product.getId(), product.getName(), product.getPrice(), quantity);
    }

    @Override
    public Long getId() {
        return productId;
    }

    @Override
    public void setId(Long aLong) {
        productId = aLong;
    }

    public int getCount() {
    return count;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
