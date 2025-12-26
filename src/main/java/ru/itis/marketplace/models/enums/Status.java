package ru.itis.marketplace.models.enums;

public enum Status {
    SHOPPING_CART("shopping-cart"),
    PENDING("pending"),
    CONFIRMED("confirmed"),
    PAID("paid"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String code;

    Status(String str) {
        this.code = str;
    }

    public String getCode() {
        return code;
    }

    public static Status fromCode(String code) {
        for (Status status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
    public boolean canBeCancelled() {
        return this == SHOPPING_CART || this == PENDING || this == CONFIRMED;
    }

}
