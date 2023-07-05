package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.usersystem.model.LoginDTO;
import de.neuefische.backend.usersystem.model.PasswordChangeDTO;
import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.model.UserRegistrationDTO;
import de.neuefische.backend.usersystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    void when_changePasswordWrongOldPassword_then_throwException() {
        //Given
        String username = "username";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword");
        UserBody savedUser = new UserBody();
        savedUser.setUsername("username");
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        savedUser.setPassword(passwordEncoder.encode("wrongPassword"));
        //When
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.of(savedUser));
        //Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.changePassword(username, passwordChangeDTO), "Incorrect Password.");
    }

    @Test
    void when_changePasswordShortNewPassword_then_throwException() {
        //Given
        String username = "username";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "short");
        UserBody savedUser = new UserBody();
        savedUser.setUsername("username");
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        savedUser.setPassword(passwordEncoder.encode("oldPassword"));
        //When
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.of(savedUser));
        //Then
        assertThrows(IllegalArgumentException.class,
                () -> userSystemService.changePassword(username, passwordChangeDTO), "Password needs to be at least 8 digits long");
    }

    @Test
    void when_changePasswordWrongUser_then_throwException() {
        //Given
        String username = "username";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword");
        //When
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.empty());
        //Then
        assertThrows(NoSuchElementException.class,
                () -> userSystemService.changePassword(username, passwordChangeDTO));
    }

    @Test
    void when_changePassword_then_Message() throws IllegalAccessException {
        //Given
        String username = "username";
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("oldPassword", "newPassword");
        UserBody savedUser = new UserBody();
        savedUser.setUsername("username");
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        savedUser.setPassword(passwordEncoder.encode("oldPassword"));
        String expected = "Password Changed";
        //When
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(savedUser)).thenReturn(savedUser);
        String actual = userSystemService.changePassword(username, passwordChangeDTO);
        //Then
        verify(userRepository).findUserBodyByUsername(username);
        verify(userRepository).save(savedUser);
        assertEquals(expected, actual);
    }

    @Test
    void when_changeAuthorityNoLead_then_throwException() {
        //Given
        List<String> authorities = List.of("ALL");
        LoginDTO loginDTO = new LoginDTO("username", List.of("ALL", "PURCHASE"));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.changeAuthorities(authorities, loginDTO));
    }

    @Test
    void when_changeAuthorityWrongUsername_then_throwException() {
        //Given
        List<String> authorities = List.of("ALL", "LEAD");
        LoginDTO loginDTO = new LoginDTO("username", List.of("ALL", "PURCHASE"));
        //When
        when(userRepository.findUserBodyByUsername("username")).thenReturn(Optional.empty());
        //Then
        assertThrows(NoSuchElementException.class,
                () -> userSystemService.changeAuthorities(authorities, loginDTO));
    }

    @Test
    void when_changeAuthority_then_returnLoginDTO() throws IllegalAccessException {
        //Given
        List<String> authorities = List.of("ALL", "LEAD");
        String username = "username";
        LoginDTO expected = new LoginDTO(username, List.of("ALL", "PURCHASE"));
        UserBody savedUser = new UserBody();
        savedUser.setUsername(username);
        UserBody changedBody = new UserBody();
        changedBody.setUsername(username);
        changedBody.setRoles(expected.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList());
        //When
        when(userRepository.findUserBodyByUsername(username)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(changedBody)).thenReturn(changedBody);
        LoginDTO actual = userSystemService.changeAuthorities(authorities, expected);
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void when_deleteUserWrongUsernameLead_then_throwException() {
        //Given
        String leadUser = "leadUser";
        String password = "password";
        String userToDelete = "user";
        //When
        when(userRepository.findUserBodyByUsername(leadUser)).thenReturn(Optional.empty());
        //Then
        assertThrows(NoSuchElementException.class,
                () -> userSystemService.deleteUser(leadUser, password, userToDelete));
    }

    @Test
    void when_deleteUserWrongPassword_then_throwException() {
        //Given
        String leadUser = "leadUser";
        String password = "password";
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String savedPassword = passwordEncoder.encode("wrong");
        String userToDelete = "user";
        UserBody savedUser = new UserBody();
        savedUser.setUsername(leadUser);
        savedUser.setPassword(savedPassword);
        savedUser.setRoles(List.of(new SimpleGrantedAuthority("LEAD")));
        when(userRepository.findUserBodyByUsername(leadUser)).thenReturn(Optional.of(savedUser));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.deleteUser(leadUser, password, userToDelete));
    }

    @Test
    void when_deleteUserNoLead_then_throwException() {
        //Given
        String leadUser = "leadUser";
        String password = "password";
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String savedPassword = passwordEncoder.encode(password);
        String userToDelete = "user";
        UserBody savedUser = new UserBody();
        savedUser.setUsername(leadUser);
        savedUser.setPassword(savedPassword);
        savedUser.setRoles(List.of(new SimpleGrantedAuthority("ALL")));
        when(userRepository.findUserBodyByUsername(leadUser)).thenReturn(Optional.of(savedUser));
        //When & Then
        assertThrows(IllegalAccessException.class,
                () -> userSystemService.deleteUser(leadUser, password, userToDelete));
    }

    @Test
    void when_deleteUser_then_returnMessage() throws IllegalAccessException {
        //Given
        String leadUser = "leadUser";
        String password = "password";
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String savedPassword = passwordEncoder.encode(password);
        String userToDelete = "user";
        UserBody savedUser = new UserBody();
        savedUser.setUsername(leadUser);
        savedUser.setPassword(savedPassword);
        savedUser.setRoles(List.of(new SimpleGrantedAuthority("LEAD")));
        when(userRepository.findUserBodyByUsername(leadUser)).thenReturn(Optional.of(savedUser));
        UserBody userBodyDelete = new UserBody();
        when(userRepository.findUserBodyByUsername(userToDelete)).thenReturn(Optional.of(userBodyDelete));
        doNothing().doThrow(new RuntimeException()).when(userRepository).delete(userBodyDelete);
        String expected = "User " + userToDelete + " deleted.";
        //When
        String actual = userSystemService.deleteUser(leadUser, password, userToDelete);
        //Then
        assertEquals(expected, actual);
    }
}
