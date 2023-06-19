package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserSystemServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserSystemService userSystemService = new UserSystemService(userRepository);

    @Test
    void when_loadUserByUsernameWithRightUsername_then_returnUser() {
        //Given
        UserBody testUser = new UserBody("testId", "testUsername", "testPassword", List.of());
        UserDetails expected = new User(testUser.getUsername(), testUser.getPassword(), List.of());
        when(userRepository.findUserBodyByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        //When
        UserDetails actual = userSystemService.loadUserByUsername(testUser.getUsername());
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_loadUserByUsernameWrongUsername_then_throwException() {
        //Given
        String wrongUsername = "wrongUsername";
        //When & Then
        assertThrows(UsernameNotFoundException.class,
                () -> userSystemService.loadUserByUsername(wrongUsername), "User with " + wrongUsername + " not found");
    }
}