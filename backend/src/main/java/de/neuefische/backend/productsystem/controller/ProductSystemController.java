package de.neuefische.backend.productsystem.controller;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/productSystem")
public class ProductSystemController {
    private final ProductSystemService productSystemService;

    @PostMapping
    public ProductBody addProductBody(@RequestBody ProductDTO productDTO) {
        return productSystemService.addProductBody(productDTO);
    }

    @GetMapping
    public List<ProductBody> getProductList() {
        return productSystemService.getProductList();
    }
}
