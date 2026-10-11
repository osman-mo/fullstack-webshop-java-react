package com.backend.webshop.controller;

import com.backend.webshop.model.*;
import com.backend.webshop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/orders/{id}/positions")
    public OrderPositionResponse createOrderPosition(@PathVariable("id") String orderId,
                                                      @Valid @RequestBody CreateOrderPositionRequest request) {
        return orderService.createNewOrderPosition(orderId, request);
    }

    @PostMapping("/orders/{id}/checkout")
    public OrderResponse checkout(@PathVariable("id") String orderId) {
        return orderService.checkout(orderId);
    }
}
