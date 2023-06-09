package de.neuefische.backend.productsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("ProductList")
public class ProductBody {
    @Id
    private String id;
    private String name;
    private double price;
    private String accessLevel;
    private String imageURL;
}
