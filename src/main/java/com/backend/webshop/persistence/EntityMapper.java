package com.backend.webshop.persistence;

import com.backend.webshop.model.*;
import java.util.List;

public final class EntityMapper {
    private EntityMapper() {}

    public static ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getPriceInCent(), List.copyOf(entity.getTags()));
    }

    public static CustomerResponse toResponse(CustomerEntity entity) {
        return new CustomerResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
    }

    public static OrderResponse toResponse(OrderEntity entity) {
        return new OrderResponse(entity.getId(), entity.getCustomer().getId(), entity.getOrderTime(),
                entity.getStatus(), List.of());
    }

    public static OrderPositionResponse toResponse(OrderPositionEntity entity) {
        return new OrderPositionResponse(entity.getId(), entity.getOrder().getId(),
                entity.getProduct().getId(), entity.getQuantity());
    }
}
