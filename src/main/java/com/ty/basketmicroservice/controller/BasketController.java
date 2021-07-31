package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.enums.BasketStatus;
import com.ty.basketmicroservice.service.BasketServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketServiceV1 basketService;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody AddItemRequest request) {
        return ResponseEntity.ok(basketService.addItem(request));
    }

    @PutMapping("/remove")
    public ResponseEntity<?> removeItem(@RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.removeItem(request));
    }

    @PutMapping("/increase")
    public ResponseEntity<?> increaseQuantity(@RequestBody ChangeQuantityRequest request) {
        return ResponseEntity.ok(basketService.increaseQuantity(request));
    }

    @PutMapping("/decrease")
    public ResponseEntity<?> decreaseQuantity(@RequestBody ChangeQuantityRequest request) {
        return ResponseEntity.ok(basketService.decreaseQuantity(request));
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeQuantity(@RequestBody ChangeQuantityRequest request) {
        return ResponseEntity.ok(basketService.changeQuantity(request));
    }

    @PutMapping("/checkbox")
    public ResponseEntity<?> checkOrUncheckItem(@RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.checkOrUncheckItem(request));
    }

    @PutMapping("/{basketId}")
    public ResponseEntity<?> completeOrder(@RequestBody Long basketId) {
        // Todo : request problem check later
        return ResponseEntity.ok(basketService.completeOrder(basketId));
    }
}
