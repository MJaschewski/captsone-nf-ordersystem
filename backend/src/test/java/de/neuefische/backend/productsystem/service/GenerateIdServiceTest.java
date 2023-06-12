package de.neuefische.backend.productsystem.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenerateIdServiceTest {

    private final GenerateIdService generateIdService = new GenerateIdService();

    @Test
    void when_generateUUID_then_returnDifferentStrings(){
        //Given
            String firstId = generateIdService.generateUUID();
        //Then
            String secondId = generateIdService.generateUUID();
        //When
            assertNotEquals(firstId,secondId);
    }

    @Test
    void when_generateUUID_then_return36LongString(){
        //Given
            int expected = 36;
        //Then
            int actual = generateIdService.generateUUID().length();
        //When
            assertEquals(expected,actual);
    }

}