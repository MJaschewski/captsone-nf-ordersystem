package de.neuefische.backend.productsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductSystemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void when_addProductBodyProductDTO_returnProductBody() throws Exception {
        //When & Then
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test",
                                    "price":1244.99,
                                    "accessLevel":"All"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test",
                            "price":1244.99,
                            "accessLevel":"All"
                        }
                        """
                ));

    }

    @Test
    void when_addProductNegativePrice_returnStatus422() throws Exception {
        //When & Then
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testBackend",
                                    "price":-1244.99,
                                    "accessLevel":"All"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
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
                                """))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void when_getProductList_then_return200OkAndListProductBody() throws Exception {
        //Given
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test1",
                                    "price":1244.99,
                                    "accessLevel":"All"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test1",
                            "price":1244.99,
                            "accessLevel":"All"
                        }
                        """
                ));
        mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test2",
                                    "price":1244.99,
                                    "accessLevel":"All"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name":"test2",
                            "price":1244.99,
                            "accessLevel":"All"
                        }
                        """
                ));
        mockMvc.perform(get("/api/productSystem"))
                .andExpect(status().isOk())
                .andExpect(content().json(("""
                        [
                            {
                                "name":"test1",
                                "price":1244.99,
                                "accessLevel":"All"
                            },
                            {
                                "name":"test2",
                                "price":1244.99,
                                "accessLevel":"All"
                            }
                        ]
                        """)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[1].id").isNotEmpty());
    }
}