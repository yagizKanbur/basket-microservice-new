package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.service.BasketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService){
        this.basketService = basketService;
    }
}
