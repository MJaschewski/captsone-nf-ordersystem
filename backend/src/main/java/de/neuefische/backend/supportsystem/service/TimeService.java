package de.neuefische.backend.supportsystem.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeService {
    private final Instant instant = Instant.now();

    public String currentDate() {
        return String.valueOf(instant).substring(0, 10);
    }
}
