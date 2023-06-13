package de.neuefische.backend.ordersystem.controller;

import de.neuefische.backend.ordersystem.service.OrderSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/OrderSystem")
public class OrderSystemController {
    private final OrderSystemService orderSystemService;
}
