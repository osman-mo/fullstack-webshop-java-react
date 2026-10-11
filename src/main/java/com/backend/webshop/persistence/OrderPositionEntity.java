package com.backend.webshop.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "order_positions")
public class OrderPositionEntity {
    @Id private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false) private OrderEntity order;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false) private ProductEntity product;
    @Column(nullable = false) private long quantity;

    protected OrderPositionEntity() {}

    public OrderPositionEntity(String id, OrderEntity order, ProductEntity product, long quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public OrderEntity getOrder() { return order; }
    public ProductEntity getProduct() { return product; }
    public long getQuantity() { return quantity; }
}
