package com.backend.webshop.repositories;

import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.*;
import com.backend.webshop.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class OrderRepository {
    private final OrderJpaRepository orders;
    private final CustomerJpaRepository customers;

    public OrderRepository(OrderJpaRepository orders, CustomerJpaRepository customers) {
        this.orders = orders;
        this.customers = customers;
    }

    public OrderResponse save(CreateOrderRequest request) {
        CustomerEntity customer = customers.findById(request.customerId())
                .orElseThrow(() -> new IdNotFoundException("Customer with id " + request.customerId() + " not found"));
        return EntityMapper.toResponse(orders.save(new OrderEntity(
                UUID.randomUUID().toString(), customer, LocalDateTime.now(), OrderStatus.NEW)));
    }

    @Transactional(readOnly = true)
    public Optional<OrderResponse> findById(String orderId) {
        return orders.findById(orderId).map(EntityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllNewOrdersByCustomerId(String customerId) {
        return orders.findAllByCustomerIdAndStatus(customerId, OrderStatus.NEW).stream()
                .map(EntityMapper::toResponse).toList();
    }

    public OrderResponse updateStatus(String orderId, OrderStatus status) {
        OrderEntity order = orders.findById(orderId)
                .orElseThrow(() -> new IdNotFoundException("Order with id " + orderId + " not found"));
        order.setStatus(status);
        return EntityMapper.toResponse(orders.save(order));
    }
}
