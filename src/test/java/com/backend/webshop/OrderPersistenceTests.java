package com.backend.webshop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test", "demo"})
class OrderPersistenceTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    // Each HTTP request has its own transaction/persistence context.
    // Reading the cart later must therefore load the stored order and positions.
    @Test
    void productAndOrderArePersistedAndLoadedIntoCart() throws Exception {
        JsonNode product = postJson("/api/products", """
                {"name":"Test keyboard","description":"USB keyboard","priceInCent":2500,"tags":["Accessories"]}
                """);
        String productId = product.get("id").asText();
        mvc.perform(get("/api/products/{id}", productId).contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0]").value("Accessories"));
        mvc.perform(get("/api/products").contextPath("/api").param("tag", "accessories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId));

        String orderId = postJson("/api/orders", "{\"customerId\":\"1\"}").get("id").asText();
        JsonNode position = postJson("/api/orders/" + orderId + "/positions",
                mapper.createObjectNode().put("productId", productId).put("quantity", 2).toString());
        assertEquals(orderId, position.get("orderId").asText());

        mvc.perform(get("/api/customers/1/shoppingcart").contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderPositions[0].orderId").value(orderId))
                .andExpect(jsonPath("$.orderPositions[0].productId").value(productId))
                .andExpect(jsonPath("$.totalCostInCent").value(5500));

        mvc.perform(post("/api/orders/{id}/checkout", orderId).contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        mvc.perform(get("/api/customers/1/shoppingcart").contextPath("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderPositions").isEmpty())
                .andExpect(jsonPath("$.deliveryCostInCent").value(0))
                .andExpect(jsonPath("$.totalCostInCent").value(0));

        String otherOrder = postJson("/api/orders", "{\"customerId\":\"1\"}").get("id").asText();
        mvc.perform(post("/api/orders/{id}/positions", otherOrder).contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.createObjectNode().put("productId", productId).put("quantity", 0).toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingCustomerCannotCreateOrder() throws Exception {
        mvc.perform(post("/api/orders").contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"customerId\":\"missing\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidProductRequestReturnsValidationError() throws Exception {
        mvc.perform(post("/api/products").contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"bad\",\"priceInCent\":0,\"tags\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    private JsonNode postJson(String path, String body) throws Exception {
        return mapper.readTree(mvc.perform(post(path).contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
    }
}
