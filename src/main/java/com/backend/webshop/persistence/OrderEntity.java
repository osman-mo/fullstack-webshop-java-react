package com.backend.webshop.persistence;

import com.backend.webshop.model.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "shop_orders")
public class OrderEntity {
    @Id private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
    @Column(name = "order_time", nullable = false) private LocalDateTime orderTime;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    protected OrderEntity() {}

    public OrderEntity(String id, CustomerEntity customer, LocalDateTime orderTime, OrderStatus status) {
        this.id = id;
        this.customer = customer;
        this.orderTime = orderTime;
        this.status = status;
    }

    public String getId() { return id; }
    public CustomerEntity getCustomer() { return customer; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}
