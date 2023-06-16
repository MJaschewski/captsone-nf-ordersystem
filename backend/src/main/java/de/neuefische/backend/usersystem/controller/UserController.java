package de.neuefische.backend.usersystem.controller;

import de.neuefische.backend.usersystem.service.UserSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("api/userSystem")
@RestController
public class UserController {

    private final UserSystemService userSystemService;

    @GetMapping("/me")
    public String getMeControllerOnly(Principal principal) {
        if (principal != null) {
            return principal.getName();
        } else {
            return "Anonymous User.";
        }
    }

    @GetMapping("/me2")
    public String getMeFromEverywhere() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/login")
    public String login() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

}
