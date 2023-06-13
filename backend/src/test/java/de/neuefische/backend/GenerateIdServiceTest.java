package de.neuefische.backend;

import de.neuefische.backend.generateId.GenerateIdService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenerateIdServiceTest {

    private final GenerateIdService generateIdService = new GenerateIdService();

    @Test
    void when_generateUUID_then_returnDifferentStrings(){
        //Given
            String firstId = generateIdService.generateUUID();
        //Then
            String secondId = generateIdService.generateUUID();
        //When
        Assertions.assertNotEquals(firstId, secondId);
    }

    @Test
    void when_generateUUID_then_return36LongString(){
        //Given
            int expected = 36;
        //Then
            int actual = generateIdService.generateUUID().length();
        //When
        Assertions.assertEquals(expected, actual);
    }

}