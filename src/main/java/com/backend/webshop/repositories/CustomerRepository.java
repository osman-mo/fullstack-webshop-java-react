package com.backend.webshop.repositories;

import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.CustomerResponse;
import com.backend.webshop.persistence.CustomerJpaRepository;
import com.backend.webshop.persistence.EntityMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomerRepository {
    private final CustomerJpaRepository customers;

    public CustomerRepository(CustomerJpaRepository customers) {
        this.customers = customers;
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(String id) {
        return customers.findById(id)
                .map(EntityMapper::toResponse)
                .orElseThrow(() -> new IdNotFoundException("Customer with id " + id + " not found"));
    }
}
