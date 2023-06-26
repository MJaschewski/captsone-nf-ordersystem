package de.neuefische.backend.ordersystem.controller;

import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderDTO;
import de.neuefische.backend.ordersystem.service.OrderSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orderSystem")
public class OrderSystemController {
    private final OrderSystemService orderSystemService;

    @PostMapping
    public OrderBody addOrder(@RequestBody OrderDTO orderDTO) {
        return orderSystemService.addOrderBody(SecurityContextHolder.getContext().getAuthentication().getName(), SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).toList(), orderDTO);
    }

    @GetMapping
    public List<OrderBody> getOrderList() {
        return orderSystemService.getOrderList();
    }

    @GetMapping("/{orderId}")
    public OrderBody getOrderById(@PathVariable String orderId) {
        return orderSystemService.getOrderById(orderId);
    }

    @PutMapping("/{orderId}")
    public OrderBody editOrder(@PathVariable String orderId, @RequestBody OrderDTO orderDTO) throws IllegalAccessException {
        return orderSystemService.editOrderById(SecurityContextHolder.getContext().getAuthentication().getName(), SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).toList(), orderId, orderDTO);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrderById(@PathVariable String orderId) throws IllegalAccessException {
        return orderSystemService.deleteOrderById(SecurityContextHolder.getContext().getAuthentication().getName(), orderId);
    }

    @PutMapping("/approve/{orderId}")
    public OrderBody approveOrderWithAuthority(@PathVariable String orderId) {
        return orderSystemService.approveOrderByIdWithAuthority(orderId, SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).toList());
    }

    @GetMapping("/own")
    public List<OrderBody> getOwnOrderList() {
        return orderSystemService.getOwnOrderList(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/own/{orderId}")
    public OrderBody getOwnOrderById(@PathVariable String orderId) throws IllegalAccessException {
        return orderSystemService.getOwnOrderById(SecurityContextHolder.getContext().getAuthentication().getName(), orderId);
    }
}
