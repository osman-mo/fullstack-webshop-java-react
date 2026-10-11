package com.backend.webshop.exception;

import org.springframework.http.HttpStatus;

public class EstoreException extends RuntimeException {
    private final HttpStatus statusCode;

    public EstoreException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
