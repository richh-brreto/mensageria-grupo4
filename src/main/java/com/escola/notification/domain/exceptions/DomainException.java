package com.escola.notification.domain.exceptions;

/**
 * Exceções de domínio genéricas.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
