package de.neuefische.backend.product_system.controller;

import de.neuefische.backend.product_system.model.ProductBody;
import de.neuefische.backend.product_system.model.ProductDTO;
import de.neuefische.backend.product_system.service.ProductSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productSystem")
public class ProductSystemController {
    private final ProductSystemService productSystemService;

    @PostMapping
    public ProductBody addProduct(@RequestBody ProductDTO productDTO){
        return productSystemService.addProduct(productDTO);
    }
}
