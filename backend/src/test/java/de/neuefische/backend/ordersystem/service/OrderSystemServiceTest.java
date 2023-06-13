package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.generateId.GenerateIdService;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderSystemServiceTest {
    private final GenerateIdService generateIdService = mock(GenerateIdService.class);
    private final ProductSystemService productSystemService = mock(ProductSystemService.class);
    private final OrderSystemService orderSystemService = new OrderSystemService(generateIdService, productSystemService);

    @Test
    void when_getProductList_then_getListProductBody() {
        //Given
        List<ProductBody> expected = List.of(new ProductBody());
        when(productSystemService.getProductList()).thenReturn(expected);
        //When
        List<ProductBody> actual = orderSystemService.getProductList();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_VerifyProductListWithRightList_then_returnTrue() {
        //Given
        ProductBody testProduct1 = new ProductBody();
        ProductBody testProduct2 = new ProductBody();
        List<ProductBody> testProductBodyList = List.of(testProduct1, testProduct2);
        when(productSystemService.getProductList()).thenReturn(testProductBodyList);
        //When
        boolean actual = orderSystemService.verifyProductList(testProductBodyList);
        //Then
        assertTrue(actual);
    }

    @Test
    void when_VerifyProductListWithWrongList_then_throwExceptionWithMessage() {
        //Given
        ProductBody testProduct1 = new ProductBody("testId", "testNotValid", 2.00, "All");
        List<ProductBody> testProductBodyList = List.of(testProduct1);
        when(productSystemService.getProductList()).thenReturn(List.of());
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.verifyProductList(testProductBodyList), "testNotValid is not a valid product.");
    }

    @Test
    void when_addOrderBody_then_returnOrderBodyAndVerifyProductList() {

    }
}