package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductSystemService {
    private final ProductRepository productRepository;
    private final GenerateIdService generateIdService;

    public ProductBody addProductBody(ProductDTO productDTO) {
        if(productDTO.getPrice() <= 0.00){
            throw new InputMismatchException("Price can't be negative");
        }
        if(productDTO.getName().equals("")){
            throw new InputMismatchException("Name can't be empty");
        }
        if(productDTO.getAccessLevel().equals("")){
            throw new InputMismatchException("Access level can't be empty");
        }
        ProductBody newProduct = new ProductBody(generateIdService.generateUUID(),productDTO.getName(),productDTO.getPrice(), productDTO.getAccessLevel());
        return productRepository.save(newProduct);
    }
}
