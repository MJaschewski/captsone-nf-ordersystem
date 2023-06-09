package de.neuefische.backend.supportsystem;

import de.neuefische.backend.supportsystem.service.TimeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeServiceTest {
    private final TimeService timeService = new TimeService();

    @Test
    void when_CurrentDate_returnStringWithRightFormat() {
        //Given
        assertTrue(timeService.currentDate().matches("\\d{4}-\\d{2}-\\d{2}"));
    }

}