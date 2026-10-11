package com.backend.webshop.repositories;

import com.backend.webshop.model.ProductCreateRequest;
import com.backend.webshop.model.ProductResponse;
import com.backend.webshop.persistence.ProductEntity;
import com.backend.webshop.persistence.ProductJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class ProductRepository {
    private final ProductJpaRepository products;

    public ProductRepository(ProductJpaRepository products) {
        this.products = products;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(String tag) {
        return products.findAll().stream()
                .filter(product -> tag == null || product.getTags().stream()
                        .anyMatch(value -> value.equalsIgnoreCase(tag)))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ProductResponse> findById(String id) {
        return products.findById(id).map(this::toResponse);
    }

    public void deleteById(String id) {
        products.deleteById(id);
    }

    public ProductResponse save(ProductCreateRequest request) {
        ProductEntity product = new ProductEntity(
                UUID.randomUUID().toString(), request.getName(), request.getDescription(),
                request.getPriceInCent(), new ArrayList<>(request.getTags()));
        return toResponse(products.save(product));
    }

    private ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPriceInCent(), new ArrayList<>(product.getTags()));
    }
}
