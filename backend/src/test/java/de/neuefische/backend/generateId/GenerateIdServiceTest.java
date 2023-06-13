package de.neuefische.backend.generateId;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GenerateIdServiceTest {

    private final GenerateIdService generateIdService = new GenerateIdService();

    @Test
    void when_generateProductUUID_then_returnDifferentStrings() {
        //Given
        String firstId = generateIdService.generateProductUUID();
        //Then
        String secondId = generateIdService.generateProductUUID();
        //When
        assertNotEquals(firstId, secondId);
    }

    @Test
    void when_generateProductUUID_then_return37LongStringAndPAtTheStart() {
        //Given
        int expected = 37;
        //Then
        String testId = generateIdService.generateProductUUID();
        int actual = testId.length();
        //When
        assertEquals('p', testId.charAt(0));
        assertEquals(expected, actual);
    }

    @Test
    void when_generateOrderUUID_then_returnDifferentStrings() {
        //Given
        String firstId = generateIdService.generateOrderUUID();
        //Then
        String secondId = generateIdService.generateOrderUUID();
        //When
        assertNotEquals(firstId, secondId);
    }

    @Test
    void when_generateOrderUUID_then_return37LongStringAndPAtTheStart() {
        //Given
        int expected = 37;
        //Then
        String testId = generateIdService.generateOrderUUID();
        int actual = testId.length();
        //When
        assertEquals('o', testId.charAt(0));
        assertEquals(expected, actual);
    }

}