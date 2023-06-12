package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.model.ProductDTO;
import de.neuefische.backend.productsystem.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class ProductSystemServiceTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final GenerateIdService generateIdService = mock(GenerateIdService.class);

    private final ProductSystemService productSystemService = new ProductSystemService(productRepository,generateIdService);

    @Test
    void when_addProductBodyWithInputAccessLevelAll_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputAll = new ProductDTO("testName", 1.00, "All");
        String testIdAll = "testIdAll";
        when(generateIdService.generateUUID()).thenReturn(testIdAll);
        ProductBody expected = new ProductBody(testIdAll, testInputAll.getName(), testInputAll.getPrice(), testInputAll.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputAll);
        //Then
        verify(generateIdService).generateUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductBodyWithInputAccessLevelPurchase_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputPurchase = new ProductDTO("testName", 1.00, "Purchase");
        String testIdPurchase = "testIdPurchase";
        when(generateIdService.generateUUID()).thenReturn(testIdPurchase);
        ProductBody expected = new ProductBody(testIdPurchase, testInputPurchase.getName(), testInputPurchase.getPrice(), testInputPurchase.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputPurchase);
        //Then
        verify(generateIdService).generateUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductBodyWithInputAccessLevelLead_then_returnProductDTOWithRightProperties() {
        //Given
        ProductDTO testInputLead = new ProductDTO("testName", 1.00, "Lead");
        String testIdLead = "testIdLead";
        when(generateIdService.generateUUID()).thenReturn(testIdLead);
        ProductBody expected = new ProductBody(testIdLead, testInputLead.getName(), testInputLead.getPrice(), testInputLead.getAccessLevel());
        when(productRepository.save(any())).thenReturn(expected);
        //When
        ProductBody actual = productSystemService.addProductBody(testInputLead);
        //Then
        verify(generateIdService).generateUUID();
        verify(productRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_addProductNegativePrice_then_returnIllegalArgumentExceptionWithMessage() {
        //Given
        ProductDTO wrongInput = new ProductDTO("testName", -3.00, "All");
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
}