package com.roomo.exception;

public class RoleUpdateException extends RuntimeException {

    public RoleUpdateException(String message) {
        super(message);
    }

    public RoleUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
