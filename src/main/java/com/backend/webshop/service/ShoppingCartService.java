package com.backend.webshop.service;

import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.*;
import com.backend.webshop.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShoppingCartService {
    private final OrderRepository orderRepository;
    private final OrderPositionRepository orderPositionRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(OrderRepository orderRepository, OrderPositionRepository orderPositionRepository,
                               ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderPositionRepository = orderPositionRepository;
        this.productRepository = productRepository;
    }

    public ShoppingCartResponse getShoppingCartForCustomer(String customerId) {
        List<OrderResponse> orders = orderRepository.findAllNewOrdersByCustomerId(customerId);
        List<String> orderIds = orders.stream().map(OrderResponse::getId).toList();
        List<OrderPositionResponse> positions = orderPositionRepository.findAllByOrderIds(orderIds);
        long deliveryCost = positions.isEmpty() ? 0 : 500;
        return new ShoppingCartResponse(customerId, positions,
                calculateSumForCart(positions, deliveryCost), deliveryCost, "STANDARD");
    }

    public long calculateSumForCart(List<OrderPositionResponse> positions, long deliveryCost) {
        return positions.stream().mapToLong(position -> {
            ProductResponse product = productRepository.findById(position.getProductId())
                    .orElseThrow(() -> new IdNotFoundException(
                            "Product with id " + position.getProductId() + " not found"));
            return position.getQuantity() * product.getPriceInCent();
        }).sum() + deliveryCost;
    }
}
