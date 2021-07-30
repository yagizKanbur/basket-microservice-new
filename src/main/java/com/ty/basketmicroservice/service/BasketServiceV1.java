package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.domain.BasketInfo;
import com.ty.basketmicroservice.domain.BasketItem;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.repository.BasketRepository;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BasketServiceV1 implements BasketService {
    private final BasketRepository basketRepository;

    public BasketServiceV1(BasketRepository basketRepository){
        this.basketRepository = basketRepository;
    }


    @Override
    public Basket increaseQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        BasketItem item = getItemFromBasket(basket, request.getProductId());

        item.increaseQuantity();

        BasketInfo info = calculatePrice(basket.getInfo(),item);

        basket.setInfo(info);

        return basketRepository.save(basket);
    }

    @Override
    public Basket decreaseQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        BasketItem item = getItemFromBasket(basket, request.getProductId());
        item.decreaseQuantity();

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
        Optional<Basket> checkBasket = basketRepository.findById(request.getBasketId());
        if (checkBasket.isEmpty()){
            Basket basket = new Basket(request);
            BasketItem item = new BasketItem(request);
            BasketInfo info = new BasketInfo();

            basket.setInfo(calculatePrice(info,item));
            basket.getItems().putIfAbsent(item.getProductId(),item);
            return basketRepository.save(basket);
        }

        Basket basket = getBasketOrElseThrow(request.getBasketId());

        if(basket.getItems().containsKey(request.getProductId())){
            ChangeQuantityRequest changeQuantityRequest = new ChangeQuantityRequest(request);
            Basket newBasket = increaseQuantity(changeQuantityRequest);
            return basketRepository.save(newBasket);
        }

        BasketItem item = new BasketItem(request);
        basket.getItems().putIfAbsent(item.getProductId(),item);
        calculatePrice(basket.getInfo(), item);
        return basketRepository.save(basket);
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
    public Basket completeOrder(Long basketId) {
        Basket basket = getBasketOrElseThrow(basketId);
        basket.changeBasketStatus();
        return basketRepository.save(basket);
    }

    public Basket getBasketOrElseThrow(Long id){
        return basketRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public BasketInfo calculatePrice(BasketInfo info ,BasketItem item){
        if (info.getSumOfProductPrices() == null){
            info.setSumOfProductPrices(item.getProductPrice());
            info.setTotalPrice();
            return info;
        }
        info.setSumOfProductPrices(item.getProductPrice() + info.getSumOfProductPrices());
        info.setTotalPrice();
        return info;
    }

    public BasketItem getItemFromBasket(Basket basket, Long id){
        return basket.getItems().get(id);
    }


}
