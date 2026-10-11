package com.backend.webshop.model;

import java.util.List;

public record ShoppingCartResponse(
        String customerId,
        List<OrderPositionResponse> orderPositions,
        long totalCostInCent,
        long deliveryCostInCent,
        String deliveryOption
) {
    public ShoppingCartResponse {
        orderPositions = List.copyOf(orderPositions);
    }
}
