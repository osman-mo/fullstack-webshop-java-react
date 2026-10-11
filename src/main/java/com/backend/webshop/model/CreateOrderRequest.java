package com.backend.webshop.model;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(@NotBlank String customerId) {
}
