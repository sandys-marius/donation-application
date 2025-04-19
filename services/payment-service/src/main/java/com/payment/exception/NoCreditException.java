package com.payment.exception;

public class NoCreditException extends RuntimeException {
    public NoCreditException(String message) {
        super(message);
    }
}
