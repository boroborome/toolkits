package com.happy3w.toolkits.exception;

/**
 * Some error may be occur in runtime system like offline, Maybe show this message to custom
 */
public class RuntimeIoException extends RuntimeException {
    public RuntimeIoException(String message) {
        super(message);
    }

    public RuntimeIoException(String message, Throwable cause) {
        super(message, cause);
    }
}
