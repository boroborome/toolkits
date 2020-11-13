package com.happy3w.toolkits.exception;

/**
 * Exception for developer
 */
public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
