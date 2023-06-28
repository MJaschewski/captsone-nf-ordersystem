package de.neuefische.backend.supportsystem;

import de.neuefische.backend.supportsystem.service.GenerateIdService;
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
        String username = "testName";
        String firstId = generateIdService.generateOrderUUID(username);
        //Then
        String secondId = generateIdService.generateOrderUUID(username);
        //When
        assertNotEquals(firstId, secondId);
    }

    @Test
    void when_generateOrderUUID_then_returnCorrectString() {
        //Given
        String testUser = "testUser";
        //Then
        String testId = generateIdService.generateOrderUUID(testUser);
        //When
        assertEquals('o', testId.charAt(0));
        assertEquals(testUser, testId.substring(11));
    }

}