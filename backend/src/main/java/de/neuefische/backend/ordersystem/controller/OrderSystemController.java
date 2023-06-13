package de.neuefische.backend.ordersystem.controller;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.service.OrderSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/OrderSystem")
public class OrderSystemController {
    private final OrderSystemService orderSystemService;

    @PostMapping
    public OrderBody addOrder(@RequestBody OrderDTO orderDTO) {
        return orderSystemService.addOrderBody(orderDTO);
    }
}
