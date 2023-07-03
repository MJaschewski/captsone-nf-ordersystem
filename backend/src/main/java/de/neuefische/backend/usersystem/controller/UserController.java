package de.neuefische.backend.usersystem.controller;

import de.neuefische.backend.usersystem.model.LoginDTO;
import de.neuefische.backend.usersystem.model.UserRegistrationDTO;
import de.neuefische.backend.usersystem.service.UserSystemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("api/userSystem")
@RestController
public class UserController {

    private final UserSystemService userSystemService;

    @PostMapping("/login")
    public LoginDTO login() {
        return new LoginDTO(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
                ,
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream().map(Object::toString).toList());
    }

    @GetMapping("/logout")
    String logout(HttpSession httpSession) {
        httpSession.invalidate();
        SecurityContextHolder.clearContext();
        return "Logged out";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginDTO registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) throws IllegalAccessException {
        return userSystemService.saveUser(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream().map(Object::toString).toList(), userRegistrationDTO);
    }

    @GetMapping("/users")
    public List<LoginDTO> getAllUsers() throws IllegalAccessException {
        return userSystemService.getAllUsers(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream().map(Object::toString).toList()
        );
    }

    @GetMapping("/users/{username}")
    public LoginDTO getUserByUsername(@PathVariable String username) throws IllegalAccessException {
        return userSystemService.getUserByUsername(username, SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(Objects::toString)
                .toList());
    }
}
