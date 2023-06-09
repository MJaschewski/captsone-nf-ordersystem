package de.neuefische.backend.product_system.service;

import de.neuefische.backend.product_system.model.ProductBody;
import de.neuefische.backend.product_system.model.ProductDTO;
import de.neuefische.backend.product_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSystemService {
    private final ProductRepository productRepository;
    private final GenerateIdService generateIdService;

    public ProductBody addProduct(ProductDTO productDTO) {
        ProductBody newProduct = new ProductBody(generateIdService.generateUUID(),productDTO.getName(),productDTO.getPrice(), productDTO.getAccessLevel());
        return productRepository.save(newProduct);
    }
}
