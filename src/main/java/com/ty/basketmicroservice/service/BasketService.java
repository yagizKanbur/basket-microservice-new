package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;

import java.util.UUID;

public interface BasketService {
    Basket increaseQuantity(ChangeQuantityRequest request);
    Basket decreaseQuantity(ChangeQuantityRequest request);
    Basket changeQuantity(ChangeQuantityRequest request);
    Basket addItem(AddItemRequest request);
    Basket removeItem(ItemRequest request);
    Basket checkOrUncheckItem(ItemRequest request);
    Basket completeOrder(Long id);
}
