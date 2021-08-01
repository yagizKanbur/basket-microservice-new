package com.ty.basketmicroservice.service;

import com.alibaba.fastjson.JSON;
import com.ty.basketmicroservice.exceptions.BasketAlreadyOrderedException;
import com.ty.basketmicroservice.exceptions.BasketNotFoundException;
import com.ty.basketmicroservice.exceptions.ItemNotFoundException;
import com.ty.basketmicroservice.model.Basket;
import com.ty.basketmicroservice.model.BasketInfo;
import com.ty.basketmicroservice.model.BasketItem;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.enums.BasketStatus;
import com.ty.basketmicroservice.repository.BasketRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceV1 implements BasketService {
    private static final String UPDATE_TOPIC = "basket.update";
    private static final String CREATE_TOPIC = "basket.create";
    private static final String DELETE_TOPIC = "basket.delete";

    // REFLECTION

    private final BasketRepository basketRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Basket increaseQuantity(ChangeQuantityRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())){
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        basket.increaseItemQuantity(request.getProductId());
        return basketRepository.save(basket);

    }

    @Override
    public Basket decreaseQuantity(ChangeQuantityRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        Basket basket = getBasketIfValid(optionalBasket);
        BasketItem item = getItemFromBasketIfExists(basket,request.getProductId());

        if (item.getQuantity() == 1) {
            ItemRequest removeRequest = ItemRequest.builder()
                    .basketId(request.getBasketId())
                    .productId(request.getProductId())
                    .sessionId(request.getSessionId())
                    .build();
            return removeItem(removeRequest);
        }

        basket.decreaseItemQuantity(item.getProductId());
        return basketRepository.save(basket);
    }

    @Override
    public Basket changeQuantity(ChangeQuantityRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }
        BasketItem item = getItemFromBasket(basket, request.getProductId());

        basket.changeItemQuantity(item.getProductId(), request.getQuantity());
        return basketRepository.save(basket);
    }

    @Override
    public Basket addItem(AddItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());

        if (optionalBasket.isEmpty() || BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            Basket createdBasket = createBasket(request);
            //kafkaTemplate.send(CREATE_TOPIC, JSON.toJSONString(createdBasket, false));
            return basketRepository.save(createdBasket);
        }

        Basket basket = optionalBasket.get();

        if (basket.getItems().containsKey(request.getProductId())) {
            ChangeQuantityRequest changeQuantityRequest = ChangeQuantityRequest.builder()
                    .basketId(request.getBasketId())
                    .productId(request.getProductId())
                    .sessionId(request.getSessionId())
                    .build();
            return increaseQuantity(changeQuantityRequest);
        }
        BasketItem item = new BasketItem(request);

        basket.addItem(item);
        //kafkaTemplate.send(UPDATE_TOPIC, JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    @Override
    public Basket removeItem(ItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())){
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }
        BasketItem item = getItemFromBasket(basket, request.getProductId());

        basket.removeItem(item.getProductId());
        //kafkaTemplate.send(UPDATE_TOPIC, JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    @Override
    public Basket checkOrUncheckItem(ItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())){
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        BasketItem item = getItemFromBasket(basket, request.getProductId());
        basket.reverseBasketItemStatus(item.getProductId());
        return basketRepository.save(basket);
    }

    @Override
    public Basket completeOrder(Long basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())){
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        basket.changeBasketStatus();
        //kafkaTemplate.send(UPDATE_TOPIC, JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    // *********************************************************************

    public Basket createBasket(AddItemRequest request) {
        Basket basket = new Basket(request);
        BasketItem item = new BasketItem(request);
        BasketInfo info = new BasketInfo();

        basket.setInfo(info);
        basket.addItem(item);
        return basket;
    }

    public BasketItem getItemFromBasket(Basket basket, Long productId) {
        return basket.getItems().get(productId);
    }

    public BasketStatus checkBasketStatus(Long basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        Basket basket = optionalBasket.get();
        return basket.getStatus();
    }

    public boolean isItemInTheBasket(Basket basket, Long productId) {
        if (basket.getItems().get(productId) != null) {
            return true;
        }
        return false;
    }

    public Basket getBasketIfValid (Optional<Basket> optionalBasket){
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())){
            throw new BasketAlreadyOrderedException();
        }
        return optionalBasket.get();
    }

    public BasketItem getItemFromBasketIfExists (Basket basket, Long productId){
        if (!isItemInTheBasket(basket, productId)) {
            throw new ItemNotFoundException();
        }
        return getItemFromBasket(basket, productId);
    }
}
