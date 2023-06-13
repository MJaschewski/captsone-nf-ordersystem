package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.generateId.GenerateIdService;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderSystemService {
    private final GenerateIdService generateIdService;
    private final ProductSystemService productSystemService;

    public List<ProductBody> getProductList() {
        return productSystemService.getProductList();
    }

    public boolean verifyProductList(List<ProductBody> testProductBodyList) {
        List<ProductBody> productBodyList = productSystemService.getProductList();
        for (ProductBody productBody : testProductBodyList) {
            if (!productBodyList.contains(productBody)) {
                throw new IllegalArgumentException(productBody.getName() + " is not a valid product.");
            }
        }
        return true;
    }
}
