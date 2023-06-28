package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.model.OrderStatus;
import de.neuefische.backend.ordersystem.repository.OrderSystemRepository;
import de.neuefische.backend.productsystem.model.AccessLevel;
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
        testProduct1.setAccessLevel(AccessLevel.ALL.toString());
        ProductBody testProduct2 = new ProductBody();
        testProduct2.setAccessLevel(AccessLevel.ALL.toString());
        List<ProductBody> testProductBodyList = List.of(testProduct1, testProduct2);
        List<String> testAuthorities = List.of("ALL");
        when(productSystemService.getProductList()).thenReturn(testProductBodyList);
        //When
        boolean actual = orderSystemService.verifyProductList(testAuthorities, testProductBodyList);
        //Then
        assertTrue(actual);
    }

    @Test
    void when_VerifyProductListWithWrongAuthority_then_ThrowException() {
        //Given
        ProductBody testProduct1 = new ProductBody();
        testProduct1.setAccessLevel(AccessLevel.ALL.toString());
        ProductBody testProduct2 = new ProductBody();
        testProduct2.setAccessLevel(AccessLevel.ALL.toString());
        List<ProductBody> testProductBodyList = List.of(testProduct1, testProduct2);
        List<String> testAuthorities = List.of("PURCHASE");
        when(productSystemService.getProductList()).thenReturn(testProductBodyList);
        //When
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.verifyProductList(testAuthorities, testProductBodyList));
    }

    @Test
    void when_VerifyProductListWithWrongList_then_throwExceptionWithMessage() {
        //Given
        ProductBody testProduct1 = new ProductBody("testId", "testNotValid", 2.00, "All");
        List<ProductBody> testProductBodyList = List.of(testProduct1);
        List<String> testAuthorities = List.of();
        when(productSystemService.getProductList()).thenReturn(List.of());
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.verifyProductList(testAuthorities, testProductBodyList), "testNotValid is not a valid product.");
    }


    @Test
    void when_addOrderBodyWrongBody_throwException() {
        //Given
        String testProductId = "testProductId";
        List<String> testAuthorities = List.of();
        ProductBody testProduct = new ProductBody(testProductId, "testProduct", 2.00, "All");
        List<ProductBody> testProductBodyList = List.of(testProduct);
        OrderDTO testOrderDTO = new OrderDTO(testProductBodyList);
        when(productSystemService.getProductList()).thenReturn(List.of());
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.addOrderBody("testOwner", testAuthorities, testOrderDTO), "testProduct is not a valid product.");

    }

    @Test
    void when_addOrderBdyEmptyProductList_throwExceptionWithMessage() {
        //Given
        OrderDTO emptyOrderDTO = new OrderDTO(List.of());
        List<String> testAuthorities = List.of();
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.addOrderBody("testOwner", testAuthorities, emptyOrderDTO), "Empty Product List.");
    }

    @Test
    void when_addOrderBody_then_returnOrderBody() {
        //Given
        String testProductId = "testProductId";
        ProductBody testProduct1 = new ProductBody(testProductId, "testProduct1", 2.00, "ALL");
        ProductBody testProduct2 = new ProductBody(testProductId, "testProduct2", 3.00, "ALL");
        List<ProductBody> testProductBodyList = List.of(testProduct1, testProduct2);
        when(productSystemService.getProductList()).thenReturn(testProductBodyList);

        OrderDTO testOrderDTO = new OrderDTO(testProductBodyList);
        String testOrderId = "testOrderId";
        when(generateIdService.generateOrderUUID("testOwner")).thenReturn(testOrderId);
        List<String> testAuthorities = List.of("ALL");
        String testDate = "2023-01-31";
        OrderBody expected = new OrderBody(testOrderId, "testOwner", testProductBodyList, 5.00, testDate, "No date yet", false, false, OrderStatus.REQUESTED.toString());

        when(timeService.currentDate()).thenReturn(testDate);
        when(orderSystemRepository.save(expected)).thenReturn(expected);
        //When
        OrderBody actual = orderSystemService.addOrderBody("testOwner", testAuthorities, testOrderDTO);
        //Then
        verify(productSystemService).getProductList();
        verify(generateIdService).generateOrderUUID("testOwner");
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
    void when_getOrderById_then_ReturnOrder() throws IllegalAccessException {
        //Given
        OrderBody expected = new OrderBody();
        String savedOrderId = "savedOrderId";
        expected.setId(savedOrderId);
        when(orderSystemRepository.findById(savedOrderId)).thenReturn(Optional.of(expected));
        //When
        OrderBody actual = orderSystemService.getOrderById(savedOrderId);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_getOrderByIdWrongId_then_throwExceptionWithError() {
        //Given
        String wrongId = "wrongId";
        when(orderSystemRepository.findById(wrongId)).thenReturn(Optional.empty());
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.getOrderById(wrongId), "No order with " + wrongId + " found.");
    }

    @Test
    void when_editOrderWrongId_then_ReturnExceptionWithMessage() {
        //Given
        String wrongId = "";
        List<String> testAuthorities = List.of();
        when(orderSystemRepository.existsById(wrongId)).thenReturn(false);
        OrderDTO testOrder = new OrderDTO();
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.editOrderById("testOwner", testAuthorities, wrongId, testOrder));

    }

    @Test
    void when_editOrder_returnChangedOrder() throws IllegalAccessException {
        //Given
        String testProductId = "testProductId";
        ProductBody testProduct1 = new ProductBody(testProductId, "testProduct1", 2.00, "ALL");
        ProductBody testProduct2 = new ProductBody(testProductId, "testProduct2", 3.00, "ALL");
        List<ProductBody> savedProductBodyList = List.of(testProduct1, testProduct2);
        List<ProductBody> newProductBodyList = List.of(testProduct1);
        when(productSystemService.getProductList()).thenReturn(savedProductBodyList);


        OrderDTO newOrderDTO = new OrderDTO(newProductBodyList);
        String savedOrderId = "savedOrderId";
        String testDate = "2023-01-31";
        String testOwner = "testOwner";
        List<String> testAuthorities = List.of("ALL");
        OrderBody orderSaved = new OrderBody(savedOrderId, testOwner, savedProductBodyList, 5.00, testDate, "No date yet", false, false, OrderStatus.REQUESTED.toString());
        OrderBody expected = new OrderBody(savedOrderId, testOwner, newProductBodyList, 2.00, testDate, "No date yet", false, false, OrderStatus.REQUESTED.toString());
        when(orderSystemRepository.save(expected)).thenReturn(expected);
        when(orderSystemRepository.findById(savedOrderId)).thenReturn(Optional.of(orderSaved));
        //When
        OrderBody actual = orderSystemService.editOrderById(testOwner, testAuthorities, savedOrderId, newOrderDTO);

        //Then
        verify(productSystemService).getProductList();
        verify(orderSystemRepository).save(expected);
        assertEquals(expected, actual);

    }

    @Test
    void when_deleteOrderById_then_returnMessage() throws IllegalAccessException {
        //Given
        String orderId = "testId";
        when(orderSystemRepository.existsById(orderId)).thenReturn(true);
        doNothing().doThrow(new RuntimeException()).when(orderSystemRepository).deleteById(orderId);
        String expected = "Deletion successful";
        OrderBody savedOrder = new OrderBody();
        savedOrder.setOwner("testOwner");
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));

        //When
        String actual = orderSystemService.deleteOrderById("testOwner", orderId);

        //Then
        verify(orderSystemRepository).existsById(orderId);
        verify(orderSystemRepository).deleteById(orderId);
        assertEquals(expected, actual);

    }

    @Test
    void when_deleteOrderByIdWrongId_then_Throw() {
        //Given
        String wrongId = "wrongId";
        when(orderSystemRepository.existsById(wrongId)).thenReturn(false);
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.deleteOrderById("testOwner", wrongId), "No order with " + wrongId + " found.");
    }

    @Test
    void when_approveOrderByIdWithAuthorityWrongId_then_Throw() {
        //Given
        String wrongId = "";
        List<String> authorities = List.of("testAuthority");
        when(orderSystemRepository.findById(wrongId)).thenReturn(Optional.empty());
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.approveOrderByIdWithAuthority(wrongId, authorities), "No order with " + wrongId + " found.");
    }

    @Test
    void when_approveOrderByIdWithAuthorityWrongAuthority_then_Throw() {
        //Given
        String wrongId = "";
        List<String> authorities = List.of("testAuthority");
        when(orderSystemRepository.findById(wrongId)).thenReturn(Optional.of(new OrderBody()));
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.approveOrderByIdWithAuthority(wrongId, authorities), "Can't read authority.");
    }

    @Test
    void when_approveOrderByIdWithAuthorityPurchase_then_returnOrderBodyApproved() {
        //Given
        String orderID = "";
        List<String> testAuthority = List.of("PURCHASE");
        OrderBody approvingOrder = new OrderBody("testId", "testOwner", List.of(), 5.00, "testDate", "testArrival", false, false, OrderStatus.REQUESTED.toString());
        when(orderSystemRepository.findById(orderID)).thenReturn(Optional.of(approvingOrder));
        approvingOrder.setApprovalPurchase(true);
        when(orderSystemRepository.save(any())).thenReturn(approvingOrder);
        //When
        OrderBody actual = orderSystemService.approveOrderByIdWithAuthority(orderID, testAuthority);
        //Then
        verify(orderSystemRepository).findById(orderID);
        verify(orderSystemRepository).save(any());
        assertEquals(approvingOrder, actual);
    }

    @Test
    void when_approveOrderByIdWithAuthorityLead_then_returnOrderBodyApproved() {
        //Given
        String orderID = "";
        List<String> testAuthority = List.of("LEAD");
        OrderBody approvingOrder = new OrderBody("testId", "testOwner", List.of(), 5.00, "testDate", "testArrival", false, false, OrderStatus.REQUESTED.toString());
        when(orderSystemRepository.findById(orderID)).thenReturn(Optional.of(approvingOrder));
        approvingOrder.setApprovalLead(true);
        when(orderSystemRepository.save(any())).thenReturn(approvingOrder);
        //When
        OrderBody actual = orderSystemService.approveOrderByIdWithAuthority(orderID, testAuthority);
        //Then
        verify(orderSystemRepository).findById(orderID);
        verify(orderSystemRepository).save(any());
        assertEquals(approvingOrder, actual);
    }

    @Test
    void when_approveOrderByIdWithAuthorityLeadAndPurchase_then_returnOrderBodyApprovedLead() {
        //Given
        String orderID = "";
        List<String> testAuthority = List.of("LEAD", "PURCHASE");
        OrderBody approvingOrder = new OrderBody("testId", "testOwner", List.of(), 5.00, "testDate", "testArrival", false, false, OrderStatus.REQUESTED.toString());
        when(orderSystemRepository.findById(orderID)).thenReturn(Optional.of(approvingOrder));
        approvingOrder.setApprovalLead(true);
        when(orderSystemRepository.save(any())).thenReturn(approvingOrder);
        //When
        OrderBody actual = orderSystemService.approveOrderByIdWithAuthority(orderID, testAuthority);
        //Then
        verify(orderSystemRepository).findById(orderID);
        verify(orderSystemRepository).save(any());
        assertEquals(approvingOrder, actual);
    }

    @Test
    void when_editOrderByIdWrongOwner_then_throwException() {
        //Given
        String wrongOwner = "wrongOwner";
        String orderId = "orderId";
        List<String> authorities = List.of();
        OrderBody savedOrder = new OrderBody();
        savedOrder.setId(orderId);
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> orderSystemService.editOrderById(wrongOwner, authorities, orderId, new OrderDTO()), "You don't own this order.");
    }

    @Test
    void when_deleteOrderByIdWrongOwner_then_throwException() {
        //Given
        String wrongOwner = "wrongOwner";
        String orderId = "orderId";
        OrderBody savedOrder = new OrderBody();
        savedOrder.setId(orderId);
        when(orderSystemRepository.existsById(orderId)).thenReturn(true);
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> orderSystemService.deleteOrderById(wrongOwner, orderId), "You don't own this order.");
    }

    @Test
    void when_getOwnOrderList_then_returnOnlyOwnOrders() {
        //Given
        String rightOwner = "rightOwner";
        String wrongOwner = "wrongOwner";
        OrderBody rightOrder1 = new OrderBody();
        rightOrder1.setOwner(rightOwner);
        OrderBody rightOrder2 = new OrderBody();
        rightOrder2.setOwner(rightOwner);
        OrderBody wrongOrder = new OrderBody();
        wrongOrder.setOwner(wrongOwner);
        List<OrderBody> allOrders = List.of(rightOrder1, rightOrder2, wrongOrder);
        when(orderSystemRepository.findAll()).thenReturn(allOrders);
        List<OrderBody> expected = List.of(rightOrder1, rightOrder2);
        //When
        List<OrderBody> actual = orderSystemService.getOwnOrderList(rightOwner);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_getOwnOrderByIdWrongId_thenThrow() {
        //Given
        String wrongId = "wrongId";
        String username = "username";
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.getOwnOrderById(username, wrongId), "No order with " + wrongId + " found.");
    }

    @Test
    void when_getOwnOrderByIdWrongUsername_thenThrow() {
        //Given
        String orderId = "wrongId";
        String wrongUsername = "username";
        OrderBody savedOrder = new OrderBody();
        savedOrder.setOwner("rightOwner");
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(savedOrder));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> orderSystemService.getOwnOrderById(wrongUsername, orderId), "Can't read orders not belonging to yourself.");
    }

    @Test
    void when_getOwnOrderById_then_returnOrderBody() throws IllegalAccessException {
        //Given
        String orderId = "wrongId";
        String username = "username";
        OrderBody expected = new OrderBody();
        expected.setOwner(username);
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(expected));
        //When
        OrderBody actual = orderSystemService.getOwnOrderById(username, orderId);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_approveOrderByIdWithAuthorityBothApproved_then_orderStatusApproved() {
        //Given
        String orderId = "orderId";
        OrderBody partlyApproved = new OrderBody();
        partlyApproved.setApprovalPurchase(true);
        List<String> authoritiesWithLead = List.of("LEAD");
        when(orderSystemRepository.findById(orderId)).thenReturn(Optional.of(partlyApproved));
        partlyApproved.setApprovalLead(true);
        partlyApproved.setOrderStatus(OrderStatus.APPROVED.toString());
        when(orderSystemRepository.save(partlyApproved)).thenReturn(partlyApproved);
        //When
        OrderBody actual = orderSystemService.approveOrderByIdWithAuthority(orderId, authoritiesWithLead);
        //Then
        assertEquals(partlyApproved, actual);
    }

    @Test
    void when_disapproveOrderWrongAuthority_then_throwException() {
        //Given
        String productId = "productId";
        List<String> authorities = List.of();
        //When & Then
        assertThrows(IllegalArgumentException.class,
                () -> orderSystemService.disapproveOrder(productId, authorities));

    }

    @Test
    void when_disapproveOrderWrongID_then_throwException() {
        //Given
        String wrongId = "wrongId";
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        //When & Then
        assertThrows(NoSuchElementException.class,
                () -> orderSystemService.disapproveOrder(wrongId, authorities));

    }

    @Test
    void when_disapproveOrder_then_String() {
        //Given
        String productId = "productId";
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        OrderBody savedOrder = new OrderBody();
        savedOrder.setId(productId);
        savedOrder.setOrderStatus(OrderStatus.REQUESTED.toString());
        when(orderSystemRepository.findById(productId)).thenReturn(Optional.of(savedOrder));
        savedOrder.setOrderStatus(OrderStatus.REJECTED.toString());
        savedOrder.setApprovalPurchase(false);
        savedOrder.setApprovalLead(false);
        when(orderSystemRepository.save(savedOrder)).thenReturn(savedOrder);
        String expected = savedOrder.getOrderStatus();
        //When
        String actual = orderSystemService.disapproveOrder(productId, authorities);
        //Then
        verify(orderSystemRepository).findById(productId);
        verify(orderSystemRepository).save(savedOrder);
        assertEquals(expected, actual);

    }
}