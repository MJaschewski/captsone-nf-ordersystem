package de.neuefische.backend.productsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductSystemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void when_addProductBodyProductDTO_returnProductBody() throws Exception {
        //When & Then
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test",
                            "price":1244.99,
                            "accessLevel":"ALL"
                        }
                        """
                ));

    }

    @Test
    @WithMockUser
    void when_addProductNegativePrice_returnStatus422() throws Exception {
        //When & Then
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testBackend",
                                    "price":-1244.99,
                                    "accessLevel":"ALL"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithMockUser
    void when_addProductWrongAccessLevel_returnStatus422() throws Exception {

        //When & Then
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testName",
                                    "price":1244.99,
                                    "accessLevel":"wrongLevel"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @WithMockUser
    void when_getProductList_then_return200OkAndListProductBody() throws Exception {
        //Given
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test1",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test1",
                            "price":1244.99,
                            "accessLevel":"ALL"
                        }
                        """
                ));
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test2",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test2",
                            "price":1244.99,
                            "accessLevel":"ALL"
                        }
                        """
                ));
        mockMvc.perform(get("/api/productSystem")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(("""
                        [
                            {
                                "name":"test1",
                                "price":1244.99,
                                "accessLevel":"ALL"
                            },
                            {
                                "name":"test2",
                                "price":1244.99,
                                "accessLevel":"ALL"
                            }
                        ]
                        """)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[1].id").isNotEmpty());
    }
}