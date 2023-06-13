package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.productsystem.model.ProductBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final List<ProductBody> productBodyList;

}
