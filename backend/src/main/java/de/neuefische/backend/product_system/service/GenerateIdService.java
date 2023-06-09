package de.neuefische.backend.product_system.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateIdService {

    public String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
