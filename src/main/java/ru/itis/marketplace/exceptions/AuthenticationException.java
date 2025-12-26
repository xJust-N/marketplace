package ru.itis.marketplace.exceptions;

import java.sql.SQLException;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable e) {
        super(e);
    }
}

