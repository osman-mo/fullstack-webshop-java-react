package com.backend.webshop.persistence;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Profile("demo")
public class DemoData implements ApplicationRunner {
    private final CustomerJpaRepository customers;
    private final ProductJpaRepository products;

    public DemoData(CustomerJpaRepository customers, ProductJpaRepository products) {
        this.customers = customers;
        this.products = products;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!customers.existsById("1")) {
            customers.save(new CustomerEntity("1", "Demo", "Customer", "demo@example.com"));
        }
        if (products.count() == 0) {
            products.saveAll(List.of(
                    new ProductEntity("demo-amd", "AMD Ryzen 9 5950X", "16-core desktop processor", 79900,
                            List.of("AMD", "CPU", "Processor")),
                    new ProductEntity("demo-intel", "Intel Core i9-9900KF", "8-core desktop processor", 33900,
                            List.of("Intel", "CPU", "Processor")),
                    new ProductEntity("demo-nvidia", "NVIDIA GeForce GTX 1080 Ti", "Graphics card with 11 GB memory", 74900,
                            List.of("NVIDIA", "GPU")),
                    new ProductEntity("demo-ram", "32 GB DDR5 Memory", "Dual-channel 6000 MT/s desktop memory", 12900,
                            List.of("Memory", "DDR5")),
                    new ProductEntity("demo-ssd", "2 TB NVMe SSD", "PCIe 4.0 solid-state drive", 14900,
                            List.of("Storage", "SSD"))
            ));
        }
    }
}
