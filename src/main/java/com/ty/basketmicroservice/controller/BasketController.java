package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.desktop.PreferencesEvent;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService){
        this.basketService = basketService;
    }

    public ResponseEntity<Basket> addItem (AddItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.addItem(request));
    }

    public ResponseEntity<Basket> removeItem(ItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.removeItem(request));
    }

    public ResponseEntity<Basket> increaseQuantity(ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.increaseQuantity(request));
    }

    public ResponseEntity<Basket> decreaseQuantity(ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.decreaseQuantity(request));
    }

    public ResponseEntity<Basket> changeQuantity(ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.changeQuantity(request));
    }

    public ResponseEntity<Basket> checkItem(ItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.checkItem(request));
    }

    public ResponseEntity<Basket> uncheckItem(ItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.uncheckItem(request));
    }

    public void completeOrder(){
        //
    }
}
