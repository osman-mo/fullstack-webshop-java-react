package com.backend.webshop.service;

import com.backend.webshop.model.OrderPositionResponse;
import com.backend.webshop.exception.IdNotFoundException;
import com.backend.webshop.model.ProductResponse;
import com.backend.webshop.repositories.OrderPositionRepository;
import com.backend.webshop.repositories.OrderRepository;
import com.backend.webshop.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartServiceTest {

    private ProductRepository productRepository;
    private ShoppingCartService service;
    @BeforeEach
    public void setupTests(){
        productRepository = mock(ProductRepository.class);
        service = new ShoppingCartService(
                mock(OrderRepository.class),
                mock(OrderPositionRepository.class),
                productRepository
        );
    }
    @Test
    public void calculateSumForEmptyCartReturnsSuppliedDeliveryCost() {

        Long result = service.calculateSumForCart(
                new ArrayList<OrderPositionResponse>(),
                500L
        );

        assertEquals(500, result);
    }


    @Test
    public void calculateSumWithOneProductSumsPriceOfProduct(){
        ProductResponse savedProduct = getSavedProduct(1000);

        List<OrderPositionResponse> orderPositions = new ArrayList<>();
        addOrderPosition(orderPositions, savedProduct, 1);

        Long result = service.calculateSumForCart(orderPositions, 500);

        assertEquals(1500, result);
    }

    @Test
    public void calculateSumWithTwoProductSumsPriceOfProduct(){
        ProductResponse savedProduct1 = getSavedProduct(1000);
        ProductResponse savedProduct2 = getSavedProduct(500);

        List<OrderPositionResponse> orderPositions = new ArrayList<>();
        addOrderPosition(orderPositions, savedProduct1, 1);
        addOrderPosition(orderPositions, savedProduct2, 4);

        Long result = service.calculateSumForCart(orderPositions, 500);

        assertEquals(3500, result);
    }

    private static void addOrderPosition(List<OrderPositionResponse> orderPositions, ProductResponse savedProduct, int quantity) {
        orderPositions.add(
                new OrderPositionResponse(
                        "1",
                        "order-id",
                        savedProduct.getId(),
                        quantity
                )
        );
    }

    private ProductResponse getSavedProduct(int price) {
        ProductResponse savedProduct = new ProductResponse(
                UUID.randomUUID().toString(), "Test product", "", price, new ArrayList<>());
        when(productRepository.findById(savedProduct.getId())).thenReturn(Optional.of(savedProduct));
        return savedProduct;
    }

    @Test
    public void testThat_calculateSumWithNonExistingProduct_throwsException() {
        ProductResponse notSavedProduct = new ProductResponse(
                "",
                "",
                "",
                1000,
                new ArrayList<>());

        List<OrderPositionResponse> orderPositions = new ArrayList<>();
        addOrderPosition(orderPositions, notSavedProduct, 1);

        when(productRepository.findById(notSavedProduct.getId())).thenReturn(Optional.empty());
        assertThrows(IdNotFoundException.class, () -> {
            Long result = service.calculateSumForCart(orderPositions, 500);
        });
    }
}
