package com.backend.webshop.persistence;

import com.backend.webshop.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findAllByCustomerIdAndStatus(String customerId, OrderStatus status);
}
