package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
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
        AccessLevel newAccessLevel = AccessLevel.valueOf(productDTO.getAccessLevel());
        newProduct.setAccessLevel(newAccessLevel.toString());

        newProduct.setId(generateIdService.generateProductUUID());
        newProduct.setName(productDTO.getName());
        newProduct.setPrice(productDTO.getPrice());

        return productRepository.save(newProduct);
    }

    public List<ProductBody> getProductList() {
        return productRepository.findAll();
    }
}
