package de.neuefische.backend.supportSystem.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateIdService {

    public String generateProductUUID() {
        return "p" + UUID.randomUUID();
    }

    public String generateOrderUUID() {
        return "o" + UUID.randomUUID();
    }
}
