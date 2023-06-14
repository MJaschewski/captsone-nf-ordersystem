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

        double orderPrice = 0;
        for (ProductBody productBody : newOrderBody.getProductBodyList()) {
            orderPrice += productBody.getPrice();
        }
        newOrderBody.setPrice(orderPrice);

        return orderSystemRepository.save(newOrderBody);
    }

    public List<OrderBody> getOrderList() {
        return orderSystemRepository.findAll();
    }

    public OrderBody getOrderById(String id){
        if(orderSystemRepository.findById(id).isPresent()){
            return orderSystemRepository.findById(id).get();
        }
        else {throw new NoSuchElementException("No order with "+id+" found.");
        }
    }
}
