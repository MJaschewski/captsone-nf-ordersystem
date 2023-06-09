package de.neuefische.backend.product_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("ProductList")
public class ProductBody {
    private String id;
    private String name;
    private double price;
    private String accessLevel;
}
