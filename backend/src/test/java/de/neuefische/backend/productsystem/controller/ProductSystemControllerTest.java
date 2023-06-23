package de.neuefische.backend.productsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.productsystem.model.ProductBody;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductSystemControllerTest {
    private static ProductBody savedProduct;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    @DirtiesContext
    void _when_addProductWrongAuthorities_return403() throws Exception {
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
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void _when_getProductsWrongAuthorities_return403() throws Exception {
        mockMvc.perform(get("/api/productSystem")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "Purchase")
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
    @DirtiesContext
    @WithMockUser(authorities = "Purchase")
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
    @DirtiesContext
    @WithMockUser(authorities = "Purchase")
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
    @DirtiesContext
    @WithMockUser(authorities = {"All", "Purchase"})
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

    @Test
    @WithMockUser(authorities = "Purchase")
    void when_editProductByIdWrongId_then_returnNotFound() throws Exception {
        //Given
        String wrongId = "wrongId";

        //When & Then
        mockMvc.perform(put("/api/productSystem/" + wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testEdit",
                                    "price":1000.99,
                                    "accessLevel":"LEAD"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(1)
    @WithMockUser(authorities = "Purchase")
    void post_ProductForOrderedTests() throws Exception {
        MvcResult postProductResult = mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        this.savedProduct = objectMapper.readValue(postProductResult.getResponse().getContentAsString(), ProductBody.class);

    }

    @Test
    @Order(2)
    @WithMockUser(authorities = {"All", "Purchase"})
    void when_getProductById_then_return200AndProduct() throws Exception {
        //Given
        String productId = savedProduct.getId();
        //When & Then
        mockMvc.perform(get("/api/productSystem/" + productId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                    "id":"%s",
                                    "name":"test",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                        """.formatted(productId)));
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = {"All", "Purchase"})
    void when_getProductByIdWrongId_then_return404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(get("/api/productSystem/" + wrongId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = "All")
    void when_getProductByIdNotPurchase_then_returnIsForbidden() throws Exception {
        //Given
        String productId = savedProduct.getId();
        //When & Then
        mockMvc.perform(get("/api/productSystem/" + productId)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "Purchase")
    void when_editProductByIdNegativePrice_then_return422() throws Exception {
        //Given
        String productId = savedProduct.getId();

        //When & Then
        mockMvc.perform(put("/api/productSystem/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testEdit",
                                    "price":-1000.99,
                                    "accessLevel":"LEAD"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "Purchase")
    void when_editProductById_then_return200OkAndEditedProduct() throws Exception {
        //Given
        String productId = savedProduct.getId();

        //When & Then
        mockMvc.perform(put("/api/productSystem/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"testEdit",
                                    "price":1000.99,
                                    "accessLevel":"LEAD"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "%s",
                            "name":"testEdit",
                            "price":1000.99,
                            "accessLevel":"LEAD"
                        }
                        """.formatted(productId)
                ));

    }

}