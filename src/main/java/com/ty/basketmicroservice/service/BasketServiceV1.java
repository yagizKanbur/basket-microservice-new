package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.domain.BasketItem;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.repository.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BasketServiceV1 implements BasketService {
    private final BasketRepository basketRepository;

    public BasketServiceV1(BasketRepository basketRepository){
        this.basketRepository = basketRepository;
    }


    @Override
    public Basket increaseQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        basket.getItems().get(request.getProductId()).increaseQuantity();
        return basketRepository.save(basket);
    }

    @Override
    public Basket decreaseQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        basket.getItems().get(request.getProductId()).decreaseQuantity();
        return basketRepository.save(basket);
    }

    @Override
    public Basket changeQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        basket.getItems().get(request.getProductId()).setQuantity(request.getQuantity());
        return basketRepository.save(basket);
    }

    @Override
    public Basket addItem(AddItemRequest request) {
        return null;
    }

    @Override
    public Basket removeItem(ItemRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        basket.getItems().remove(request.getProductId());
        return basketRepository.save(basket);
    }

    @Override
    public Basket checkOrUncheckItem(ItemRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        basket.getItems().get(request.getProductId()).changeCheckBox();
        return basketRepository.save(basket);
    }

    @Override
    public Basket completeOrder(UUID basketId) {
        Basket basket = getBasketOrElseThrow(basketId);
        basket.changeBasketStatus();
        return basketRepository.save(basket);
    }

    public Basket getBasketOrElseThrow(UUID uuid){
        return basketRepository.findById(uuid).orElseThrow(RuntimeException::new);
    }


}
