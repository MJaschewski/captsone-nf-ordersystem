package de.neuefische.backend.ordersystem.model;

import de.neuefische.backend.productsystem.model.ProductBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private List<ProductBody> productBodyList;
}
