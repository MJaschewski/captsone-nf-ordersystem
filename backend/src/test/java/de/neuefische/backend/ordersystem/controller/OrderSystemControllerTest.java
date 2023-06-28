package de.neuefische.backend.ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.backend.ordersystem.model.OrderBody;
import de.neuefische.backend.ordersystem.model.OrderStatus;
import de.neuefische.backend.productsystem.model.ProductBody;
import de.neuefische.backend.supportsystem.service.TimeService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderSystemControllerTest {

    private final TimeService timeService = new TimeService();
    private static OrderBody tempOrder = new OrderBody();
    private static ProductBody tempNewProduct = new ProductBody();

    @Autowired
    MockMvc mockMvc;

    @Test
    @DirtiesContext
    @WithMockUser
    void when_orderSystemWrongAuthority_then403() throws Exception {
        mockMvc.perform(get("/api/orderSystem")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_addOrderBody_then_return201_returnOrderBody() throws Exception {

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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
                        """.formatted(addedOrder.getId(), addedOrder.getProductBodyList().get(0).getId(), addedOrder.getProductBodyList().get(0).getName(), addedOrder.getProductBodyList().get(0).getAccessLevel(), timeService.currentDate())));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "testUser", authorities = {"ALL", "PURCHASE"})
    void when_getOwnOrderById_return200OkAndOrderBody() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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

        mockMvc.perform(get("/api/orderSystem/own/" + addedOrder.getId())
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
                        """.formatted(addedOrder.getId(), addedOrder.getProductBodyList().get(0).getId(), addedOrder.getProductBodyList().get(0).getName(), addedOrder.getProductBodyList().get(0).getAccessLevel(), timeService.currentDate())));
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_getOrderByIdWrongId_then_return404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(get("/api/orderSystem/" + wrongId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_deleteOrderByIdWrongId_throw404() throws Exception {
        //Given
        String wrongId = "wrongId";
        //When & Then
        mockMvc.perform(delete("/api/orderSystem/" + wrongId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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
                .andExpect(status().isOk())
                .andExpect(content().string("Deletion successful"));

    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_approveOrderWithAuthorityPurchase_returnApprovedOrder() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
        mockMvc.perform(put("/api/orderSystem/approve/" + addedOrder.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
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
                            "arrival": "No date yet",
                            "approvalPurchase": true,
                            "approvalLead": false,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(addedOrder.getId(), newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())));

    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE", "LEAD"})
    void when_approveOrderWithAuthorityLead_returnApprovedOrder() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
        mockMvc.perform(put("/api/orderSystem/approve/" + addedOrder.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
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
                            "arrival": "No date yet",
                            "approvalPurchase": false,
                            "approvalLead": true,
                            "orderStatus": "REQUESTED"
                        }
                        """.formatted(addedOrder.getId(), newProduct.getId(), newProduct.getName(), newProduct.getAccessLevel(), timeService.currentDate())));

    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = "ALL")
    void when_approveOrderWithAuthorityWrong_returnIsForbidden() throws Exception {
        //When&Then
        mockMvc.perform(put("/api/orderSystem/approve/" + "testId")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_disapproveOrder_returnMessage() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
        mockMvc.perform(put("/api/orderSystem/disapprove/" + addedOrder.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(OrderStatus.REJECTED.toString()));

    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_disapproveOrderWrongId_return404() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
        mockMvc.perform(put("/api/orderSystem/disapprove/" + "wrongId")
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @Test
    @DirtiesContext
    @WithMockUser(authorities = {"ALL", "PURCHASE"})
    void when_addOrderBodyWithNoBody_then_ThrowException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orderSystem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                """)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    @WithMockUser(username = "postUser", authorities = {"ALL", "PURCHASE"})
    void when_postOrder_for_orderedTests() throws Exception {
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
                .andExpect(status().isCreated())
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
        this.tempNewProduct = newProduct;

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
                .andExpect(status().isCreated())
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

        this.tempOrder = objectMapper.readValue(orderResult.getResponse().getContentAsString(), OrderBody.class);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "getUser", authorities = {"ALL", "PURCHASE"})
    void when_editOrderByIDWrongOwner_returnIsForbidden_get() throws Exception {
        //When & Then
        mockMvc.perform(put("/api/orderSystem/" + tempOrder.getId())
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
                                """.formatted(tempNewProduct.getId(), tempNewProduct.getName(), tempNewProduct.getAccessLevel()))
                        .with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(2)
    @WithMockUser(username = "getUser", authorities = {"ALL", "PURCHASE"})
    void when_deleteOrderByIDWrongOwner_returnIsForbidden_get() throws Exception {
        //When & Then
        mockMvc.perform(delete("/api/orderSystem/" + tempOrder.getId()).with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(2)
    @WithMockUser(username = "wrongUser", authorities = {"ALL", "PURCHASE"})
    void when_getOwnOrderByIDWrongOwner_returnIsForbidden_get() throws Exception {
        //When & Then
        mockMvc.perform(get("/api/orderSystem/own/" + tempOrder.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(2)
    @WithMockUser(username = "postUser", authorities = {"ALL", "PURCHASE"})
    void when_getOwnOrderByIDWrongId_returnNotFound_get() throws Exception {
        //When & Then
        mockMvc.perform(get("/api/orderSystem/own/" + "wrongId")
                        .with(csrf()))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(2)
    @WithMockUser(username = "newUser", authorities = {"ALL", "PURCHASE"})
    void when_getOwnOrderList_returnOwnedOrderList() throws Exception {
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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
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
        mockMvc.perform(get("/api/orderSystem/own")
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
                        , addedOrder.getProductBodyList().get(0).getId()
                        , addedOrder.getProductBodyList().get(0).getName()
                        , addedOrder.getProductBodyList().get(0).getAccessLevel()
                        , addedOrder.getCreated()
                        , addedOrder.getArrival()
                        , addedOrder.getOrderStatus())
                ));
    }

    @Test
    @WithMockUser(authorities = "ALL")
    @Order(3)
    @DirtiesContext
    void when_getOwnOrderListNoOrders_return_200AndEmptyList() throws Exception {
        mockMvc.perform(get("/api/orderSystem/own")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                        [
                        ]
                        """));
    }
}
