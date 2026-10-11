package com.backend.webshop.exception;

import java.time.Instant;

public record ErrorInfo(String error, String path, Instant timestamp) {
}
