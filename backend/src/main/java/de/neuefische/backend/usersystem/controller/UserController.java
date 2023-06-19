package de.neuefische.backend.usersystem.controller;

import de.neuefische.backend.usersystem.service.UserSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/userSystem")
@RestController
public class UserController {

    private final UserSystemService userSystemService;

    @PostMapping("/login")
    public String login() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

}