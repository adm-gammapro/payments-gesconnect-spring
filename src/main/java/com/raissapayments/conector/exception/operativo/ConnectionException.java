package com.raissapayments.conector.exception.operativo;

public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}