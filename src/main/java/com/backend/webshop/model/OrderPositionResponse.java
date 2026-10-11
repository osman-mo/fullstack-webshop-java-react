package com.backend.webshop.model;

public class OrderPositionResponse {
    private final String id;
    private final String orderId;
    private final String productId;
    private final long quantity;

    public OrderPositionResponse(String id, String orderId, String productId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public long getQuantity() { return quantity; }
}
