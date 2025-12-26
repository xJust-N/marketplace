package ru.itis.marketplace.models.enums;

public enum UserRole {
    CUSTOMER("customer"),
    SELLER("seller"),
    CUSTOMER_AND_SELLER("customer-and-seller"),
    ADMIN("admin"),
    MODERATOR("moderator");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    public boolean isSeller() {
        return this.equals(SELLER) || this.equals(CUSTOMER_AND_SELLER);
    }

    public boolean isCustomerOnly() {
        return this.equals(CUSTOMER);
    }

    public boolean haveEditRights() {
        return this.equals(ADMIN) || this.equals(MODERATOR);
    }

    public String getCode() {
        return code;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }

}