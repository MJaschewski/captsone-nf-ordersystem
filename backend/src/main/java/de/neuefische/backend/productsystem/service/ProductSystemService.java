package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


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
        newProduct.setImageURL(productDTO.getImageURL());

        return productRepository.save(newProduct);
    }

    public List<ProductBody> getProductList() {
        return productRepository.findAll();
    }

    public ProductBody getProductById(String productId) {
        return productRepository.findById(productId).orElseThrow();
    }


    public ProductBody editProductById(String productId, ProductDTO productDTO) {
        ProductBody savedProduct = productRepository.findById(productId).orElseThrow();

        if (productDTO.getPrice() <= 0.00) {
            throw new IllegalArgumentException("Price can't be negative");
        }
        savedProduct.setAccessLevel(AccessLevel.valueOf(productDTO.getAccessLevel()).toString());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setName(productDTO.getName());
        savedProduct.setImageURL(productDTO.getImageURL());
        return productRepository.save(savedProduct);
    }

    public String deleteProductById(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("No product with id " + productId + "found.");
        }
        productRepository.deleteById(productId);
        return "Deletion successful";
    }
}
