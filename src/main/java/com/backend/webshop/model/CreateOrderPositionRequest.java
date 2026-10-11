package com.backend.webshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderPositionRequest(
        @NotBlank String productId,
        @Positive long quantity
) {
}
