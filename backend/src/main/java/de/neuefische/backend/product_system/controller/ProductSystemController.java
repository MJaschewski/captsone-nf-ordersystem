package de.neuefische.backend.product_system.controller;

import de.neuefische.backend.product_system.service.ProductSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productSystem")
public class ProductSystemController {
    private final ProductSystemService productSystemService;
}
