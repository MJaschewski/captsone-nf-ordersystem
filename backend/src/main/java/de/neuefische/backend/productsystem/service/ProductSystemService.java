package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductSystemService {
    private final ProductRepository productRepository;
    private final GenerateIdService generateIdService;

    public ProductBody addProductBody(ProductDTO productDTO) {
        if (productDTO.getPrice() <= 0.00) {
            throw new IllegalArgumentException("Price can't be negative");
        }
        if (productDTO.getName().equals("")) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        ProductBody newProduct = new ProductBody();
        switch (productDTO.getAccessLevel()) {
            case "All" -> newProduct.setAccessLevel(AccessLevel.ALL);
            case "Purchase" -> newProduct.setAccessLevel(AccessLevel.PURCHASE);
            case "Lead" -> newProduct.setAccessLevel(AccessLevel.LEAD);
            default -> throw new IllegalArgumentException("Not a valid access level");
        }

        newProduct.setId(generateIdService.generateUUID());
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());

        return productRepository.save(newProduct);
    }
}
