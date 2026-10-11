package com.backend.webshop.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class ProductCreateRequest {

    @NotBlank private final String name;
    private final String description;
    @NotNull @Positive private final Integer priceInCent;
    @NotNull private final List<String> tags;

    public ProductCreateRequest(
            String name,
            String description,
            Integer priceInCent,
            List<String> tags) {
        this.name = name;
        this.description = description == null ? "" : description;
        this.priceInCent = priceInCent;
        this.tags = tags == null ? List.of() : List.copyOf(tags);
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPriceInCent() {
        return priceInCent;
    }

    public List<String> getTags() {
        return tags;
    }


}
