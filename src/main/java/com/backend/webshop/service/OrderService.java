package com.backend.webshop.service;

import com.backend.webshop.exception.EstoreException;
import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.*;
import com.backend.webshop.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPositionRepository orderPositionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderPositionRepository orderPositionRepository,
                        CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderPositionRepository = orderPositionRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        customerRepository.findById(request.customerId());
        return orderRepository.save(request);
    }

    @Transactional
    public OrderPositionResponse createNewOrderPosition(String orderId, CreateOrderPositionRequest request) {
        OrderResponse order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotFoundException("Order with id " + orderId + " not found"));
        if (order.getStatus() != OrderStatus.NEW) {
            throw new EstoreException("Only NEW orders can be changed", HttpStatus.CONFLICT);
        }
        productRepository.findById(request.productId())
                .orElseThrow(() -> new IdNotFoundException("Product with id " + request.productId() + " not found"));

        return orderPositionRepository.save(new OrderPositionResponse(
                UUID.randomUUID().toString(), orderId, request.productId(), request.quantity()));
    }

    @Transactional
    public OrderResponse checkout(String orderId) {
        OrderResponse order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IdNotFoundException("Order with id " + orderId + " not found"));
        if (order.getStatus() != OrderStatus.NEW) {
            throw new EstoreException("Order has already been checked out", HttpStatus.CONFLICT);
        }
        return orderRepository.updateStatus(orderId, OrderStatus.CONFIRMED);
    }
}
