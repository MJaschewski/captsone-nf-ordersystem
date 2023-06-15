package de.neuefische.backend.ordersystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.supportsystem.service.TimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderSystemControllerTest {

    private final TimeService timeService = new TimeService();

    @Autowired
    MockMvc mockMvc;

    @Test
    void when_addOrderBody_then_return200Ok_returnOrderBody() throws Exception {

        MvcResult postProduct = mockMvc.perform(post("/api/productSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""" 
                                {
                                    "name":"test",
                                    "price":1244.99,
                                    "accessLevel":"ALL"
                                }
                                """))
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
                        ))
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
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
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
                                """))
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
                        ))
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
        mockMvc.perform(get("/api/orderSystem"))
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
                                """))
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
                        ))
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

        mockMvc.perform(get("/api/orderSystem/"+addedOrder.getId()))
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
    void when_getOrderByIdWrongId_then_return404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(get("/api/orderSystem/"+wrongId))
                .andExpect(status().isNotFound());
    }
}