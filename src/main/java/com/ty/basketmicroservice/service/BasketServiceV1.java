package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.repository.BasketRepository;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceV1 implements BasketService {
    private final BasketRepository basketRepository;

    public BasketServiceV1(BasketRepository basketRepository){
        this.basketRepository = basketRepository;
    }


    @Override
    public Basket increaseQuantity(ChangeQuantityRequest request) {
        return null;
    }

    @Override
    public Basket decreaseQuantity(ChangeQuantityRequest request) {
        return null;
    }

    @Override
    public Basket changeQuantity(ChangeQuantityRequest request) {
        return null;
    }

    @Override
    public Basket addItem(AddItemRequest request) {
        return null;
    }

    @Override
    public Basket removeItem(ItemRequest request) {
        return null;
    }

    @Override
    public Basket checkItem(ItemRequest request) {
        return null;
    }

    @Override
    public Basket uncheckItem(ItemRequest request) {
        return null;
    }

    @Override
    public Basket completeOrder() {
        return null;
    }
}
