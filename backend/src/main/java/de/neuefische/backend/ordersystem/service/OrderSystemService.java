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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


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

    public boolean verifyProductList(List<String> accessLevel, List<ProductBody> testProductBodyList) {
        List<ProductBody> productBodyList = productSystemService.getProductList();
        for (ProductBody productBody : testProductBodyList) {
            if (!productBodyList.contains(productBody)) {
                throw new IllegalArgumentException(productBody.getName() + " is not a valid product.");
            }
            if (!accessLevel.contains(productBody.getAccessLevel())) {
                throw new IllegalArgumentException("You are not authorized to order product " + productBody.getName() + ".");
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

    public OrderBody addOrderBody(String username, List<String> accessLevel, OrderDTO orderDTO) {
        if (orderDTO.getProductBodyList().equals(List.of())) {
            throw new IllegalArgumentException("Empty Product List.");
        }
        verifyProductList(accessLevel, orderDTO.getProductBodyList());
        OrderBody newOrderBody = new OrderBody();
        newOrderBody.setOwner(username);
        newOrderBody.setId(generateIdService.generateOrderUUID(username));
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

    public OrderBody getOrderById(String orderId) {
        return orderSystemRepository.findById(orderId).orElseThrow();
    }

    public OrderBody editOrderById(String username, List<String> accessLevel, String orderId, OrderDTO orderDTO) throws IllegalAccessException {
        OrderBody oldOrderBody = orderSystemRepository.findById(orderId).orElseThrow();
        if (!Objects.equals(orderSystemRepository.findById(orderId).orElseThrow().getOwner(), username)) {
            throw new IllegalAccessException("Can not edit Order you don't own.");
        }
        verifyProductList(accessLevel, orderDTO.getProductBodyList());

        oldOrderBody.setId(orderId);
        oldOrderBody.setProductBodyList(orderDTO.getProductBodyList());
        oldOrderBody.setOrderStatus(OrderStatus.REQUESTED.toString());
        oldOrderBody.setArrival("No date yet");
        oldOrderBody.setApprovalPurchase(false);
        oldOrderBody.setApprovalPurchase(false);
        oldOrderBody.setPrice(calculatePrice(orderDTO.getProductBodyList()));

        return orderSystemRepository.save(oldOrderBody);
    }

    public String deleteOrderById(String username, String orderId) throws IllegalAccessException {
        if (!orderSystemRepository.existsById(orderId)) {
            throw new NoSuchElementException("No order with " + orderId + " found.");
        }
        if (!Objects.equals(orderSystemRepository.findById(orderId).orElseThrow().getOwner(), username)) {
            throw new IllegalAccessException("You can't delete orders you don't own.");
        }
        orderSystemRepository.deleteById(orderId);
        return "Deletion successful";
    }

    public OrderBody approveOrderByIdWithAuthority(String orderId, List<String> authorities) {
        OrderBody approvingOrder = orderSystemRepository.findById(orderId).orElseThrow();
        if (authorities.contains(AccessLevel.LEAD.toString())) {
            approvingOrder.setApprovalLead(true);
        } else if (authorities.contains(AccessLevel.PURCHASE.toString())) {
            approvingOrder.setApprovalPurchase(true);
        } else throw new IllegalArgumentException("Can't read authority.");
        if (approvingOrder.isApprovalLead() && approvingOrder.isApprovalPurchase()) {
            approvingOrder.setOrderStatus(OrderStatus.APPROVED.toString());
        }
        return orderSystemRepository.save(approvingOrder);
    }

    public List<OrderBody> getOwnOrderList(String username) {
        List<OrderBody> allOrders = orderSystemRepository.findAll();
        List<OrderBody> ownOrders = new ArrayList<>();
        allOrders.forEach(orderBody -> {
            if (orderBody.getOwner().equals(username)) {
                ownOrders.add(orderBody);
            }
        });
        return ownOrders;

    }

    public OrderBody getOwnOrderById(String username, String orderId) throws IllegalAccessException {
        OrderBody savedOrder = orderSystemRepository.findById(orderId).orElseThrow();
        if (!savedOrder.getOwner().equals(username)) {
            throw new IllegalAccessException("Can't read orders not belonging to yourself.");
        }
        return savedOrder;
    }

    public String disapproveOrder(String orderId, List<String> authorities) {
        if (!(authorities.contains(AccessLevel.PURCHASE.toString()) || authorities.contains(AccessLevel.LEAD.toString()))) {
            throw new IllegalArgumentException("No authorization to reject order");
        }
        OrderBody savedOrder = orderSystemRepository.findById(orderId).orElseThrow();
        savedOrder.setOrderStatus(OrderStatus.REJECTED.toString());
        savedOrder.setApprovalLead(false);
        savedOrder.setApprovalPurchase(false);
        return orderSystemRepository.save(savedOrder).getOrderStatus();
    }
}
