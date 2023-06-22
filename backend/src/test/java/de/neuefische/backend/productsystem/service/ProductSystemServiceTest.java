package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductSystemServiceTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final GenerateIdService generateIdService = mock(GenerateIdService.class);

    private final ProductSystemService productSystemService = new ProductSystemService(productRepository,generateIdService);

    @Test
    void when_addProductBodyWithInputAccessLevelAll_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputAll = new ProductDTO("testName", 1.00, "ALL");
        String testIdAll = "testIdAll";
        when(generateIdService.generateProductUUID()).thenReturn(testIdAll);
        ProductBody expected = new ProductBody(testIdAll, testInputAll.getName(), testInputAll.getPrice(), testInputAll.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputAll);
        //Then
        verify(generateIdService).generateProductUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductBodyWithInputAccessLevelPurchase_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputPurchase = new ProductDTO("testName", 1.00, "PURCHASE");
        String testIdPurchase = "testIdPurchase";
        when(generateIdService.generateProductUUID()).thenReturn(testIdPurchase);
        ProductBody expected = new ProductBody(testIdPurchase, testInputPurchase.getName(), testInputPurchase.getPrice(), testInputPurchase.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputPurchase);
        //Then
        verify(generateIdService).generateProductUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductBodyWithInputAccessLevelLead_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputLead = new ProductDTO("testName", 1.00, "LEAD");
        String testIdLead = "testIdLead";
        when(generateIdService.generateProductUUID()).thenReturn(testIdLead);
        ProductBody expected = new ProductBody(testIdLead, testInputLead.getName(), testInputLead.getPrice(), testInputLead.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputLead);
        //Then
        verify(generateIdService).generateProductUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductNegativePrice_then_returnIllegalArgumentExceptionWithMessage() {
        //Given
        ProductDTO wrongInput = new ProductDTO("testName", -3.00, "ALL");
        //When & Then
        assertThrows(IllegalArgumentException.class
                , () -> productSystemService.addProductBody(wrongInput), "Price can't be negative");
    }

    @Test
    void when_addProductWrongAccessLevel_then_returnIllegalArgumentExceptionWithMessage() {
        //Given
        ProductDTO wrongInput = new ProductDTO("testName", 3.00, "WrongLevel");
        //When & Then
        assertThrows(IllegalArgumentException.class
                , () -> productSystemService.addProductBody(wrongInput), "Not a valid access level");
    }

    @Test
    void when_getProductList_returnProductList() {
        //Given
        List<ProductBody> expected = List.of(new ProductBody());
        when(productRepository.findAll()).thenReturn(expected);
        //When
        List<ProductBody> actual = productSystemService.getProductList();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_getProductByIdWrongId_thenThrowException() {
        //Given
        String wrongId = "wrongId";
        when(productRepository.findById(wrongId)).thenReturn(Optional.empty());
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> productSystemService.getProductById(wrongId));
    }

    @Test
    void when_getProductById_returnProduct() {
        //Given
        String productId = "productId";
        ProductBody expected = new ProductBody();
        when(productRepository.findById(productId)).thenReturn(Optional.of(expected));
        //When
        ProductBody actual = productSystemService.getProductById(productId);
        //Then
        assertEquals(expected, actual);
    }

}