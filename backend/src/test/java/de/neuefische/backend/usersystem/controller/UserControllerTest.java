package de.neuefische.backend.usersystem.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @WithMockUser(username = "username", password = "password", authorities = "All")
    void when_loginCorrectUser_then_return200AndUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/login")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                         {"username":"username",
                         "authorities":["All"]
                         }
                        """
                ));
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

    @Test
    @DirtiesContext
    @WithMockUser
    void when_registerWrongAuthority_then_return403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register", """
                                {}
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "LEAD")
    void when_registerShortPassword_then_return422() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"testUser",
                                    "password":"short",
                                    "roles":["ALL"]
                                }
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "LEAD")
    void when_registerShortUsername_then_return422() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"short",
                                    "password":"passwordTest",
                                    "roles":["ALL"]
                                }
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "LEAD")
    void when_registerWithoutAll_then_return422() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"username",
                                    "password":"passwordTest",
                                    "roles":[]
                                }
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "LEAD")
    void when_register_then_return200OkAndLoginDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"username",
                                    "password":"passwordTest",
                                    "authorities":["ALL"]
                                }
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                        {
                                            "username":"username",
                                            "authorities":["ALL"]
                                        }
                                """));
    }

    @Test
    @WithMockUser(authorities = "LEAD")
    @Order(1)
    void post_user_orderedTests() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/userSystem/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"username",
                                    "password":"passwordTest",
                                    "authorities":["ALL"]
                                }
                                 """)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                        {
                                            "username":"username",
                                            "authorities":["ALL"]
                                        }
                                """));
    }

    @Test
    @WithMockUser
    @Order(2)
    void when_getAllUsersNoLead_return403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/userSystem/users"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "LEAD")
    @Order(2)
    void when_getAllUsers_return200OkAndListLoginDTO() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/userSystem/users")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "username":"username",
                                "authorities":["ALL"]
                            }
                        ]
                        """));
    }

    @Test
    @WithMockUser(username = "username", password = "passwordTest", authorities = "ALL")
    @Order(2)
    void when_changePassword_return200OkAndChangedPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/userSystem/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "oldPassword":"passwordTest",
                                        "newPassword":"testChangePassword"
                                    }
                                """).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Password Changed and logged out"));
    }
}