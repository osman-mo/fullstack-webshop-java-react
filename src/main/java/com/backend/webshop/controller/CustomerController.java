package com.backend.webshop.controller;

import com.backend.webshop.model.CustomerResponse;
import com.backend.webshop.model.ShoppingCartResponse;
import com.backend.webshop.repositories.CustomerRepository;
import com.backend.webshop.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ShoppingCartService shoppingCartService;

    public CustomerController(CustomerRepository customerRepository, ShoppingCartService shoppingCartService) {
        this.customerRepository = customerRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse getCustomerById(@PathVariable String id) {
        return customerRepository.findById(id);
    }

    @GetMapping("/customers/{id}/shoppingcart")
    public ShoppingCartResponse getShoppingCartByCustomerId(@PathVariable String id) {
        customerRepository.findById(id);
        return shoppingCartService.getShoppingCartForCustomer(id);
    }
}
