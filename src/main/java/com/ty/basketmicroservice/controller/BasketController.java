package com.ty.basketmicroservice.controller;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.service.BasketServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketServiceV1 basketService;

    @PostMapping
    public ResponseEntity<?> addItem(@Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(basketService.addItem(request));
    }

    @PutMapping("/remove")
    public ResponseEntity<?> removeItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.removeItem(request));
    }

    @PutMapping("/increase")
    public ResponseEntity<?> increaseQuantity(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.increaseQuantity(request));
    }

    @PutMapping("/decrease")
    public ResponseEntity<?> decreaseQuantity(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.decreaseQuantity(request));
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeQuantity(@Valid @RequestBody ChangeQuantityRequest request) {
        return ResponseEntity.ok(basketService.changeQuantity(request));
    }

    @PutMapping("/checkbox")
    public ResponseEntity<?> checkOrUncheckItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(basketService.checkOrUncheckItem(request));
    }

    @PutMapping("/order")
    public ResponseEntity<?> completeOrder(@Valid @RequestBody String basketId) {
        return ResponseEntity.ok(basketService.completeOrder(basketId));
    }
}
