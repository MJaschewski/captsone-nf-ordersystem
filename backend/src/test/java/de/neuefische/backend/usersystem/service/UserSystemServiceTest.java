package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.usersystem.model.LoginDTO;
import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.model.UserRegistrationDTO;
import de.neuefische.backend.usersystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSystemServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GenerateIdService generateIdService = mock(GenerateIdService.class);
    private final UserSystemService userSystemService = new UserSystemService(userRepository, generateIdService);

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

    @Test
    void when_saveUserNoLead_then_throwException() {
        //Given
        List<String> authorities = List.of();
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        //When
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.saveUser(authorities, userRegistrationDTO), "No authority lead.");
    }

    @Test
    void when_saveUserShortPassword_then_throwException() {
        //Given
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("short");
        //When
        assertThrows(IllegalArgumentException.class,
                () -> userSystemService.saveUser(authorities, userRegistrationDTO), "Password needs to be at least 8 digits long.");
    }

    @Test
    void when_saveUserShortUsername_then_throwException() {
        //Given
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setUsername("short");
        when(userRepository.existsByUsername("short")).thenReturn(false);
        //When
        assertThrows(IllegalArgumentException.class,
                () -> userSystemService.saveUser(authorities, userRegistrationDTO), "Username needs to be at least 8 digits long and needs to be unique");
    }

    @Test
    void when_saveUserNoAllAuthority_then_throwException() {
        //Given
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setPassword("password");
        String username = "username";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        userRegistrationDTO.setUsername(username);
        userRegistrationDTO.setAuthorities(List.of());
        //When
        assertThrows(IllegalArgumentException.class,
                () -> userSystemService.saveUser(authorities, userRegistrationDTO), "User must have authority All.");
    }

    @Test
    void when_saveUser_then_returnLoginDTO() throws IllegalAccessException {
        //Given
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("password");
        String username = "username";
        userRegistrationDTO.setUsername(username);
        userRegistrationDTO.setAuthorities(List.of("ALL"));
        LoginDTO expected = new LoginDTO(username, List.of(AccessLevel.ALL.toString()));
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(generateIdService.generateUserUUID()).thenReturn("testId");
        when(userRepository.save(any())).thenReturn(new UserBody());
        //When
        LoginDTO actual = userSystemService.saveUser(authorities, userRegistrationDTO);
        //Then
        verify(userRepository).existsByUsername(username);
        verify(generateIdService).generateUserUUID();
        verify(userRepository).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void when_saveUserTakenUsername_then_throwException() {
        //Given
        List<String> authorities = List.of(AccessLevel.LEAD.toString());
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setUsername("takenUsername");
        when(userRepository.existsByUsername("takenUsername")).thenReturn(true);
        //When
        assertThrows(IllegalArgumentException.class,
                () -> userSystemService.saveUser(authorities, userRegistrationDTO), "Username needs to be at least 8 digits long and needs to be unique");
    }

    @Test
    void when_authorityLeadCheckNoLead_then_throwException() {
        //Given
        List<String> authorities = List.of("ALL");
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.authorityLeadCheck(authorities), "No authority lead.");
    }

    @Test
    void when_getAllUsersWithoutLead_then_throwException() {
        //Given
        List<String> authorities = List.of("ALL");
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.getAllUsers(authorities), "No authority lead.");
    }

    @Test
    void when_getAllUsers_then_returnListLoginDTO() throws IllegalAccessException {
        //Given
        List<String> authorities = List.of("ALL", "LEAD");
        UserBody testUser = new UserBody();
        testUser.setUsername("testUser");
        testUser.setRoles(List.of(new SimpleGrantedAuthority("ALL")));
        LoginDTO expectedUser = new LoginDTO(testUser.getUsername(), testUser.getRoles().stream().map(Objects::toString).toList());
        List<LoginDTO> expected = List.of(expectedUser);
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        //When
        List<LoginDTO> actual = userSystemService.getAllUsers(authorities);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_getUserByUsernameWrongName_then_throwException() {
        //Given
        String wrongUsername = "wrongUser";
        when(userRepository.findUserBodyByUsername(wrongUsername)).thenReturn(Optional.empty());
        //When & then
        assertThrows(NoSuchElementException.class,
                () -> userSystemService.getUserByUsername(wrongUsername), "User with " + wrongUsername + " not found");
    }

    @Test
    void when_getUserByUsername_then_returnLoginDTO() {
        //Given
        String username = "username";
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ALL"));
        UserBody savedUser = new UserBody();
        savedUser.setUsername(username);
        savedUser.setRoles(authorities);
        LoginDTO expected = new LoginDTO(username, authorities.stream().map(Objects::toString).toList());
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.of(savedUser));
        //When
        LoginDTO actual = userSystemService.getUserByUsername(username);
        //Then
        assertEquals(expected, actual);
    }
}
