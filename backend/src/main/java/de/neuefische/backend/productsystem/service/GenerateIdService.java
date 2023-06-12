package de.neuefische.backend.productsystem.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateIdService {

    public String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
