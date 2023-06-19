package de.neuefische.backend.usersystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void when_loginWrongUser_then_return401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/login")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "username", password = "password")
    void when_loginCorrectUser_then_return200AndUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/login")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("username"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "username", password = "password")
    void when_logout_then_return200AndMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/userSystem/logout")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Logged out"));
    }
}