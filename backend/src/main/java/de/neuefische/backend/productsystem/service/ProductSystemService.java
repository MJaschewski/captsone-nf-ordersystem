package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import de.neuefische.backend.supportSystem.GenerateIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductSystemService {
    private final ProductRepository productRepository;
    private final GenerateIdService generateIdService;

    public ProductBody addProductBody(ProductDTO productDTO) {
        if (productDTO.getPrice() <= 0.00) {
            throw new IllegalArgumentException("Price can't be negative");
        }
        ProductBody newProduct = new ProductBody();
        switch (productDTO.getAccessLevel()) {
            case "All" -> newProduct.setAccessLevel("All");
            case "Purchase" -> newProduct.setAccessLevel("Purchase");
            case "Lead" -> newProduct.setAccessLevel("Lead");
            default -> throw new IllegalArgumentException("Not a valid access level");
        }

        newProduct.setId(generateIdService.generateProductUUID());
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());

        return productRepository.save(newProduct);
    }

    public List<ProductBody> getProductList() {
        return productRepository.findAll();
    }
}
