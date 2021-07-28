package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
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

    public void addItem (AddItemRequest request){
        // Todo: Add Item Return Cart
    }

    public void removeItem(ItemRequest request){
        // Todo: Remove Item Return Cart
    }

    public void increaseQuantity(ChangeQuantityRequest request){
        // Todo: Increase quantity of an item
    }

    public void decreaseQuantity(ChangeQuantityRequest request){
        // Todo: Decrease quantity of an item
    }

    public void changeQuantity(ChangeQuantityRequest request){
        // Todo: change quantity of an item to the given number
    }

    public void checkItem(ItemRequest request){
        // check checkbox for an given item
    }

    public void uncheckItem(ItemRequest request){
        // uncheck checkbox for given item
    }

    public void completeOrder(){
        //
    }
}
