package com.backend.webshop.controller;

import com.backend.webshop.model.ProductCreateRequest;
import com.backend.webshop.model.ProductResponse;
import com.backend.webshop.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts(@RequestParam(required = false) String tag) {

        return productRepository.findAll(tag);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id){
        Optional<ProductResponse> product = productRepository.findById(id);
        if(product.isPresent())
            return ResponseEntity.ok(product.get());
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id){
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/products")
    public ProductResponse createProduct(@Valid @RequestBody ProductCreateRequest request){
        return productRepository.save(request);
    }
}
