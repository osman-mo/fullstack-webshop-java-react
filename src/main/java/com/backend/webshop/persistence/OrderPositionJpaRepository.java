package com.backend.webshop.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderPositionJpaRepository extends JpaRepository<OrderPositionEntity, String> {
    List<OrderPositionEntity> findAllByOrderIdIn(List<String> orderIds);
}
