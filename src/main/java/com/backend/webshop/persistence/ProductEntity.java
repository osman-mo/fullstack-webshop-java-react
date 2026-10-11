package com.backend.webshop.persistence;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id private String id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, length = 2000) private String description;
    @Column(name = "price_in_cent", nullable = false) private int priceInCent;
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag", nullable = false)
    @OrderColumn(name = "tag_index")
    private List<String> tags = new ArrayList<>();

    protected ProductEntity() {}

    public ProductEntity(String id, String name, String description, int priceInCent, List<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceInCent = priceInCent;
        this.tags = new ArrayList<>(tags);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPriceInCent() { return priceInCent; }
    public List<String> getTags() { return tags; }
}
