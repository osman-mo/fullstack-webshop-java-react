package com.backend.webshop.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private final String id;
    private final String customerId;
    private final LocalDateTime orderTime;
    private final OrderStatus status;
    private final List<OrderPositionResponse> orderPositionResponses;

    public OrderResponse(String id, String customerId, LocalDateTime orderTime, OrderStatus status,
                         List<OrderPositionResponse> orderPositionResponses) {
        this.id = id;
        this.customerId = customerId;
        this.orderTime = orderTime;
        this.status = status;
        this.orderPositionResponses = List.copyOf(orderPositionResponses);
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public OrderStatus getStatus() { return status; }
    public List<OrderPositionResponse> getOrderPositionResponses() { return orderPositionResponses; }
}
