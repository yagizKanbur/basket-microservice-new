package com.ty.basketmicroservice.controller;

import com.alibaba.fastjson.JSON;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.model.Basket;
import com.ty.basketmicroservice.service.BasketServiceV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = BasketController.class)
@ActiveProfiles("test")
class BasketControllerTest {
    private static final String BASKET_ID = "a9ef89a7-bbf4-4cd9-9b22-85b90d6e147d";
    private static final Long SESSION_ID = 10001L;
    private static final Long PRODUCT_ID = 10001L;
    private static final Double GENERIC_PRICE = 4.00;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketServiceV1 basketService;

    @Test
    void addItem() throws Exception {
        AddItemRequest request = AddItemRequest.builder()
                .basketId(BASKET_ID).sessionId(SESSION_ID)
                .productId(PRODUCT_ID).productPrice(GENERIC_PRICE)
                .productImage("str").productInfo("str").build();
        String requestString = request.toString();


        //this.mockMvc.perform(post("http://localhost:8080/basket/", JSON.toJSONString(requestString))).andExpect(status().isOk());
        //this.mockMvc.perform(post("http://localhost:8081/basket/", request.toString())).andExpect(status().isOk());
    }


    @Test
    void increaseQuantity() throws Exception {
        ItemRequest request = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        //JSON.toJSONString(request);

        //this.mockMvc.perform(put("/basket/increase", request)).andExpect(status().isOk());
    }

}