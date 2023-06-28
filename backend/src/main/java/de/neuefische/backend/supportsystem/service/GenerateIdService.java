package de.neuefische.backend.supportsystem.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateIdService {

    public String generateProductUUID() {
        return "p" + UUID.randomUUID();
    }

    public String generateOrderUUID(String username) {
        return "o-" + UUID.randomUUID().toString().substring(0, 8) + "-" + username;
    }
}
