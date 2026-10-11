package com.backend.webshop.exception;

import org.springframework.http.HttpStatus;

public class IdNotFoundException extends EstoreException {
    public IdNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
