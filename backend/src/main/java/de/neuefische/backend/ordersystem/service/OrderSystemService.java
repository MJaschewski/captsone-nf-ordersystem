package de.neuefische.backend.ordersystem.service;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.model.OrderStatus;
import de.neuefische.backend.ordersystem.repository.OrderSystemRepository;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.productsystem.service.ProductSystemService;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.supportsystem.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class OrderSystemService {

    private final TimeService timeService;
    private final GenerateIdService generateIdService;
    private final ProductSystemService productSystemService;
    private final OrderSystemRepository orderSystemRepository;

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

    public double calculatePrice(List<ProductBody> productBodyList){
        if(productBodyList.equals(List.of())){
            throw new IllegalArgumentException("Product list can't be empty.");
        }
        double orderPrice = 0;
        for (ProductBody productBody : productBodyList) {
            orderPrice += productBody.getPrice();
        }
        if(orderPrice < 0){
            throw new IllegalArgumentException("Price can't be negative. Change product list.");
        }
        return orderPrice;
    }

    public OrderBody addOrderBody(OrderDTO orderDTO) {
        if (orderDTO.getProductBodyList().equals(List.of())) {
            throw new IllegalArgumentException("Empty Product List.");
        }
        verifyProductList(orderDTO.getProductBodyList());
        OrderBody newOrderBody = new OrderBody();
        newOrderBody.setId(generateIdService.generateOrderUUID());
        newOrderBody.setProductBodyList(orderDTO.getProductBodyList());
        newOrderBody.setCreated(timeService.currentDate());
        newOrderBody.setArrival("No date yet");
        newOrderBody.setApprovalLead(false);
        newOrderBody.setApprovalPurchase(false);
        newOrderBody.setOrderStatus(OrderStatus.REQUESTED.toString());
        newOrderBody.setPrice(calculatePrice(orderDTO.getProductBodyList()));

        return orderSystemRepository.save(newOrderBody);
    }

    public List<OrderBody> getOrderList() {
        return orderSystemRepository.findAll();
    }

    public OrderBody editOrder(String orderId, OrderDTO orderDTO) {
        if(!orderSystemRepository.existsById(orderId)){
            throw new NoSuchElementException("No order with this id.");
        }
        verifyProductList(orderDTO.getProductBodyList());
        orderSystemRepository.findById(orderId)
                .ifPresent(oldOrderBody -> {
                    oldOrderBody.setProductBodyList(orderDTO.getProductBodyList());
                    oldOrderBody.setOrderStatus(OrderStatus.REQUESTED.toString());
                    oldOrderBody.setArrival("No date yet");
                    oldOrderBody.setApprovalPurchase(false);
                    oldOrderBody.setApprovalPurchase(false);
                    oldOrderBody.setPrice(calculatePrice(orderDTO.getProductBodyList()));
                });
        return orderSystemRepository.findById(orderId).get();
    }
}
