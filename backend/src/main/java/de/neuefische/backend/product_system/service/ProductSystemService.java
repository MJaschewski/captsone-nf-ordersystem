package de.neuefische.backend.product_system.service;

import de.neuefische.backend.product_system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSystemService {
    private final ProductRepository productRepository;
}
