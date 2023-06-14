package de.neuefische.backend.ordersystem.controller;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.service.OrderSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orderSystem")
public class OrderSystemController {
    private final OrderSystemService orderSystemService;

    @PostMapping
    public OrderBody addOrder(@RequestBody OrderDTO orderDTO) {
        return orderSystemService.addOrderBody(orderDTO);
    }

    @GetMapping
    public List<OrderBody> getOrderList(){
        return orderSystemService.getOrderList();
    }

    @PutMapping("/{orderId}")
    public OrderBody editOrder(@PathVariable String orderId,@RequestBody OrderDTO orderDTO){
        return orderSystemService.editOrder(orderId,orderDTO);
    }
}
