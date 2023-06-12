package de.neuefische.backend.productsystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
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
    void when_addProductBodyWithInput_then_returnProductDTOWithRightProperties(){
        //Given
            ProductDTO testInput = new ProductDTO("testName", 1.00, "All");
        String testId = "testId";
            when(generateIdService.generateUUID()).thenReturn(testId);
        ProductBody expected = new ProductBody(testId, testInput.getName(), testInput.getPrice(), AccessLevel.ALL);
            when(productRepository.save(any())).thenReturn(expected);
        //When
            ProductBody actual = productSystemService.addProductBody(testInput);
        //Then
            verify(generateIdService).generateUUID();
            verify(productRepository).save(any());
            assertEquals(expected,actual);
    }

    @Test
    void when_addProductNegativePrice_then_returnIllegalArgumentExceptionWithMessage(){
        //Given
        ProductDTO wrongInput = new ProductDTO("testName", -3.00, "All");
        //When & Then
            assertThrows(IllegalArgumentException.class
                    , ()->productSystemService.addProductBody(wrongInput),"Price can't be negative");
    }
}