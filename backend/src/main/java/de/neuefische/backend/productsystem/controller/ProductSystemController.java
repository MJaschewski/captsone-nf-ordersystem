package de.neuefische.backend.productsystem.controller;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/productSystem")
public class ProductSystemController {
    private final ProductSystemService productSystemService;

    @PostMapping
    public ProductBody addProductBody(@RequestBody ProductDTO productDTO){
        return productSystemService.addProductBody(productDTO);
    }
}
