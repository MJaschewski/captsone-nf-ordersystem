package de.neuefische.backend.ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.supportsystem.service.TimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderSystemControllerTest {

    private final TimeService timeService = new TimeService();

    @Autowired
    MockMvc mockMvc;

    @Test
    @DirtiesContext
    @WithMockUser
    void when_addOrderBody_then_return200Ok_returnOrderBody() throws Exception {

        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
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
                )).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ProductBody newProduct = objectMapper.readValue(postProduct.getResponse().getContentAsString(), ProductBody.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())))
                .andExpect(jsonPath("$.productBodyList[0].id").isNotEmpty());
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_addOrderBodyInvalidProduct_then_ThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "wrongId",
                                            "name": "testWrong",
                                            "price": 1244.99,
                                            "accessLevel": "wrongLevel"
                                        }
                                        ]
                                    }
                                """)
                        .with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void when_getOrderList_then_return200OkAndOrderList() throws Exception {
        //Given
        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
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
                )).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ProductBody newProduct = objectMapper.readValue(postProduct.getResponse().getContentAsString(), ProductBody.class);

        MvcResult orderResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())))
                .andExpect(jsonPath("$.productBodyList[0].id").isNotEmpty()).andReturn();

        OrderBody addedOrder = objectMapper.readValue(orderResult.getResponse().getContentAsString(), OrderBody.class);

        //When & Then
        mockMvc.perform(get("/api/orderSystem")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                [
                                                        {
                                    "id": "%s",
                                    "productBodyList": [
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                            "accessLevel": "%s"
                        }
                    ],
                    "price": 1244.99,
                    "created": "%s",
                    "arrival": "%s",
                    "approvalPurchase": false,
                    "approvalLead": false,
                    "orderStatus": "%s"
                                        }
                ]
                """.formatted(addedOrder.getId()
                        ,addedOrder.getProductBodyList().get(0).getId()
                        ,addedOrder.getProductBodyList().get(0).getName()
                        ,addedOrder.getProductBodyList().get(0).getAccessLevel()
                        ,addedOrder.getCreated()
                        ,addedOrder.getArrival()
                        ,addedOrder.getOrderStatus())
                ));

    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_getOrderById_return200OkAndOrderBody() throws Exception {
        //Given
        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
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
                )).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ProductBody newProduct = objectMapper.readValue(postProduct.getResponse().getContentAsString(), ProductBody.class);

        MvcResult orderResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())))
                .andExpect(jsonPath("$.productBodyList[0].id").isNotEmpty()).andReturn();

        OrderBody addedOrder = objectMapper.readValue(orderResult.getResponse().getContentAsString(), OrderBody.class);

        mockMvc.perform(get("/api/orderSystem/" + addedOrder.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"%s",
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(addedOrder.getId(),addedOrder.getProductBodyList().get(0).getId(), addedOrder.getProductBodyList().get(0).getName(), addedOrder.getProductBodyList().get(0).getAccessLevel(), timeService.currentDate())));
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_getOrderByIdWrongId_then_return404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(get("/api/orderSystem/" + wrongId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void when_editOrderByIdWrongId_then_return404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(put("/api/orderSystem/" + wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "wrongId",
                                            "name": "testWrong",
                                            "price": 1244.99,
                                            "accessLevel": "wrongLevel"
                                        }
                                        ]
                                    }
                                """)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_editOrderById_then_return200OkAndChangedOrder() throws Exception {
        //Given
        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
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
                )).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ProductBody newProduct = objectMapper.readValue(postProduct.getResponse().getContentAsString(), ProductBody.class);

        MvcResult orderResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())))
                .andExpect(jsonPath("$.productBodyList[0].id").isNotEmpty()).andReturn();

        OrderBody addedOrder = objectMapper.readValue(orderResult.getResponse().getContentAsString(), OrderBody.class);

        //When&Then
        mockMvc.perform(put("/api/orderSystem/" + addedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        },
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel()
                                , newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id":"%s",
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                },
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 2489.98,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(
                        addedOrder.getId()
                        , addedOrder.getProductBodyList().get(0).getId()
                        , addedOrder.getProductBodyList().get(0).getName()
                        , addedOrder.getProductBodyList().get(0).getAccessLevel()
                        , addedOrder.getProductBodyList().get(0).getId()
                        , addedOrder.getProductBodyList().get(0).getName()
                        , addedOrder.getProductBodyList().get(0).getAccessLevel()
                        , timeService.currentDate())));

    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_deleteOrderByIdWrongId_throw404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(delete("/api/orderSystem/" + wrongId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "wrongId",
                                            "name": "testWrong",
                                            "price": 1244.99,
                                            "accessLevel": "wrongLevel"
                                        }
                                        ]
                                    }
                                """)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithMockUser
    void when_deleteOrderById_then_return200OkAndMessage() throws Exception {
        //Given
        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
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
                )).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        ProductBody newProduct = objectMapper.readValue(postProduct.getResponse().getContentAsString(), ProductBody.class);

        MvcResult orderResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "productBodyList":[
                                        {
                                            "id": "%s",
                                            "name": "%s",
                                            "price": 1244.99,
                                            "accessLevel": "%s"
                                        }
                                        ]
                                    }
                                """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel())
                        )
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "productBodyList": [
                                {
                                    "id": "%s",
                                    "name": "%s",
                                    "price": 1244.99,
                                    "accessLevel": "%s"
                                }
                            ],
                            "price": 1244.99,
                            "created": "%s",
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())))
                .andExpect(jsonPath("$.productBodyList[0].id").isNotEmpty()).andReturn();

        OrderBody addedOrder = objectMapper.readValue(orderResult.getResponse().getContentAsString(), OrderBody.class);

        //When&Then
        mockMvc.perform(delete("/api/orderSystem/" + addedOrder.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletion successful"));

    }

}
