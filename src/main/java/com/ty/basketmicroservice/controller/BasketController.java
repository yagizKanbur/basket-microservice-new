package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.enums.BasketStatus;
import com.ty.basketmicroservice.service.BasketServiceV1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketServiceV1 basketService;

    public BasketController(BasketServiceV1 basketService){
        this.basketService = basketService;
    }

    @PostMapping
    public ResponseEntity<?> addItem (@RequestBody AddItemRequest request){
        if(basketService.checkBasketStatus(request.getBasketId()) == BasketStatus.ORDERED){
            return null;
        }
        return ResponseEntity.ok(basketService.addItem(request));
    }

    @PutMapping("/remove")
    public ResponseEntity<Basket> removeItem(@RequestBody ItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.removeItem(request));
    }

    @PutMapping("/increase")
    public ResponseEntity<Basket> increaseQuantity(@RequestBody ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.increaseQuantity(request));
    }

    @PutMapping("/decrease")
    public ResponseEntity<Basket> decreaseQuantity(@RequestBody ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.decreaseQuantity(request));
    }

    @PutMapping("/change")
    public ResponseEntity<Basket> changeQuantity(@RequestBody ChangeQuantityRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.changeQuantity(request));
    }

    @PutMapping("/checkbox")
    public ResponseEntity<Basket> checkOrUncheckItem(@RequestBody ItemRequest request){
        // Todo:Control of the request
        return ResponseEntity.ok(basketService.checkOrUncheckItem(request));
    }

    @PostMapping("/order")
    public ResponseEntity<Basket> completeOrder(@RequestBody Long id){
        return ResponseEntity.ok(basketService.completeOrder(id));
    }
}
