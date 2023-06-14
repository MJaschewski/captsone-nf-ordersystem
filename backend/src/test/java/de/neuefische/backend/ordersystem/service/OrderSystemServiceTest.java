package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.model.OrderStatus;
import de.neuefische.backend.ordersystem.repository.OrderSystemRepository;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.supportsystem.service.TimeService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderSystemServiceTest {
    private final GenerateIdService generateIdService = mock(GenerateIdService.class);
    private final ProductSystemService productSystemService = mock(ProductSystemService.class);
    private final TimeService timeService = mock(TimeService.class);
    private final OrderSystemRepository orderSystemRepository = mock(OrderSystemRepository.class);
    private final OrderSystemService orderSystemService = new OrderSystemService(timeService, generateIdService, productSystemService, orderSystemRepository);

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
    void when_addOrderBodyWrongBody_throwException() {
        //Given
        String testProductId = "testProductId";
        ProductBody testProduct = new ProductBody(testProductId, "testProduct", 2.00, "All");
        List<ProductBody> testProductBodyList = List.of(testProduct);
        OrderDTO testOrderDTO = new OrderDTO(testProductBodyList);
        when(productSystemService.getProductList()).thenReturn(List.of());
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.addOrderBody(testOrderDTO), "testProduct is not a valid product.");

    }

    @Test
    void when_addOrderBdyEmptyProductList_throwExceptionWithMessage() {
        //Given
        OrderDTO emptyOrderDTO = new OrderDTO(List.of());
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.addOrderBody(emptyOrderDTO), "Empty Product List.");
    }

    @Test
    void when_addOrderBody_then_returnOrderBody() {
        //Given
        String testProductId = "testProductId";
        ProductBody testProduct1 = new ProductBody(testProductId, "testProduct1", 2.00, "All");
        ProductBody testProduct2 = new ProductBody(testProductId, "testProduct2", 3.00, "All");
        List<ProductBody> testProductBodyList = List.of(testProduct1, testProduct2);
        when(productSystemService.getProductList()).thenReturn(testProductBodyList);

        OrderDTO testOrderDTO = new OrderDTO(testProductBodyList);
        String testOrderId = "testOrderId";
        when(generateIdService.generateOrderUUID()).thenReturn(testOrderId);
        String testDate = "2023-01-31";
        OrderBody expected = new OrderBody(testOrderId, testProductBodyList, 5.00, testDate, "No date yet", false,false, OrderStatus.REQUESTED.toString());

        when(timeService.currentDate()).thenReturn(testDate);
        when(orderSystemRepository.save(expected)).thenReturn(expected);
        //When
        OrderBody actual = orderSystemService.addOrderBody(testOrderDTO);
        //Then
        verify(productSystemService).getProductList();
        verify(generateIdService).generateOrderUUID();
        verify(timeService).currentDate();
        verify(orderSystemRepository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void when_getOrderList_then_returnOrderList(){
        //Given
        List<OrderBody> expected = List.of(new OrderBody());
        when(orderSystemRepository.findAll()).thenReturn(expected);
        //When
        List<OrderBody> actual = orderSystemService.getOrderList();
        //Then
        assertEquals(expected,actual);

    }

    @Test
    void when_calculatePriceListProducts_then_ReturnPrice(){
        //Given
        ProductBody testProduct1 = new ProductBody();
        ProductBody testProduct2 = new ProductBody();
        double testPrice1 = 1.00;
        double testPRice2 = 2.00;
        testProduct1.setPrice(testPrice1);
        testProduct2.setPrice(testPRice2);
        List<ProductBody> testList = List.of(testProduct1,testProduct2);
        double expected = testPrice1+testPRice2;
        //When
        double actual = orderSystemService.calculatePrice(testList);
        //Then
        assertEquals(expected,actual);
    }

    @Test
    void when_calculatePriceNoProducts_then_ThrowException(){
        //Given
        List<ProductBody> emptyList = List.of();
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.calculatePrice(emptyList),"Product list can't be empty.");
    }

    @Test
    void when_calculatePriceNegativeResult_then_ThrowException(){
        //Given
        ProductBody negativePriceProduct = new ProductBody();
        negativePriceProduct.setPrice(-1.00);
        List<ProductBody> testList = List.of(negativePriceProduct);
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.calculatePrice(testList),"Price can't be negative. Change product list.");
    }

    @Test
    void when_editOrderWrongId_then_ReturnExceptionWithMessage(){
        //Given
        String wrongId = "";
        when(orderSystemRepository.existsById(wrongId)).thenReturn(false);
        OrderDTO testOrder = new OrderDTO();
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.editOrder(wrongId,testOrder), "No order with this id.");

    }

    @Test
    void when_editOrder_returnChangedOrder(){
        //Given
        String testProductId = "testProductId";
        ProductBody testProduct1 = new ProductBody(testProductId, "testProduct1", 2.00, "All");
        ProductBody testProduct2 = new ProductBody(testProductId, "testProduct2", 3.00, "All");
        List<ProductBody> savedProductBodyList = List.of(testProduct1, testProduct2);
        List<ProductBody> newProductBodyList = List.of(testProduct1);
        when(productSystemService.getProductList()).thenReturn(savedProductBodyList);



        OrderDTO newOrderDTO = new OrderDTO(newProductBodyList);
        String savedOrderId = "savedOrderId";
        when(generateIdService.generateOrderUUID()).thenReturn(savedOrderId);
        String testDate = "2023-01-31";
        OrderBody orderSaved = new OrderBody(savedOrderId, savedProductBodyList, 5.00, testDate, "No date yet", false,false, OrderStatus.REQUESTED.toString());
        OrderBody expected = new OrderBody(savedOrderId, newProductBodyList, 2.00, testDate, "No date yet", false,false, OrderStatus.REQUESTED.toString());


        //When
        when(orderSystemRepository.existsById(savedOrderId)).thenReturn(true);
        when(orderSystemRepository.findById(savedOrderId)).thenReturn(Optional.of(orderSaved));
        when(orderSystemRepository.findById(savedOrderId).isPresent()).thenReturn(true);
        OrderBody actual = orderSystemService.editOrder(savedOrderId,newOrderDTO);

        //Then
        assertEquals(expected,actual);

    }

    @Test
    void when_getOrderById_then_ReturnOrder(){
        //Given
        OrderBody expected = new OrderBody();
        String savedOrderId = "savedOrderId";
        expected.setId(savedOrderId);
        when(orderSystemRepository.findById(savedOrderId)).thenReturn(Optional.of(expected));
       //When
        OrderBody actual = orderSystemService.getOrderById(savedOrderId);
        //Then
        assertEquals(expected,actual);
    }

    @Test
    void when_getOrderByIdWrongId_then_throwExceptionWithError(){
        //Given
        String wrongId = "wrongId";
        when(orderSystemRepository.findById(wrongId)).thenReturn(Optional.empty());
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.getOrderById(wrongId),"No order with "+wrongId+" found.");
    }
}