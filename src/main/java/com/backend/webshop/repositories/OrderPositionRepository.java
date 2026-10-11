package com.backend.webshop.repositories;

import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.OrderPositionResponse;
import com.backend.webshop.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderPositionRepository {
    private final OrderPositionJpaRepository positions;
    private final OrderJpaRepository orders;
    private final ProductJpaRepository products;

    public OrderPositionRepository(OrderPositionJpaRepository positions, OrderJpaRepository orders,
                                   ProductJpaRepository products) {
        this.positions = positions;
        this.orders = orders;
        this.products = products;
    }

    public OrderPositionResponse save(OrderPositionResponse position) {
        OrderEntity order = orders.findById(position.getOrderId())
                .orElseThrow(() -> new IdNotFoundException("Order with id " + position.getOrderId() + " not found"));
        ProductEntity product = products.findById(position.getProductId())
                .orElseThrow(() -> new IdNotFoundException("Product with id " + position.getProductId() + " not found"));
        return EntityMapper.toResponse(positions.save(new OrderPositionEntity(
                position.getId(), order, product, position.getQuantity())));
    }

    @Transactional(readOnly = true)
    public List<OrderPositionResponse> findAllByOrderIds(List<String> orderIds) {
        if (orderIds.isEmpty()) return List.of();
        return positions.findAllByOrderIdIn(orderIds).stream().map(EntityMapper::toResponse).toList();
    }
}
