package de.neuefische.backend.productsystem.service;

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
        ProductBody newProduct = new ProductBody(generateIdService.generateUUID(),productDTO.getName(),productDTO.getPrice(), productDTO.getAccessLevel());
        return productRepository.save(newProduct);
    }
}
