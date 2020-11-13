package com.happy3w.toolkits.exception;

/**
 * Error to show to custom
 */
public class CustomMessageException extends RuntimeException {
    public CustomMessageException(String message) {
        super(message);
    }

    public CustomMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
