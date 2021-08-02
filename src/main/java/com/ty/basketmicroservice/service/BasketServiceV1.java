package com.ty.basketmicroservice.service;

import com.alibaba.fastjson.JSON;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import com.ty.basketmicroservice.exceptions.*;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceV1 implements BasketService {
    private static final String UPDATE_TOPIC = "basket.update";
    private static final String CREATE_TOPIC = "basket.create";
    private static final String ORDER_TOPIC = "basket.order";
    private static final int DEFAULT_STARTING_QUANTITY = 1;

    // REFLECTION

    private final BasketRepository basketRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Basket increaseQuantity(ItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        BasketItem item = getItemFromBasket(basket, request.getProductId());
        basket.increaseItemQuantity(item);
        return basketRepository.save(basket);
    }

    @Override
    public Basket decreaseQuantity(ItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        BasketItem item = getItemFromBasket(basket, request.getProductId());

        if (item.getQuantity() == 1) {
            return removeItem(request);
        }

        basket.decreaseItemQuantity(item);
        return basketRepository.save(basket);
    }

    @Override
    public Basket changeQuantity(ChangeQuantityRequest request) {
        if (request.getQuantity() <= 0) {
            throw new NegativeQuantityException();
        }

        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());

        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }

        Basket basket = optionalBasket.get();

        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        BasketItem item = getItemFromBasket(basket, request.getProductId());

        basket.changeItemQuantity(item, request.getQuantity());
        return basketRepository.save(basket);
    }

    @Override
    public Basket addItem(AddItemRequest request) {
        // Todo: Create basket if there is no basket id in the request
        if(request.getProductPrice() <= 0){
            throw new NegativePriceException();
        }

        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());

        if (optionalBasket.isEmpty() || BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            Basket createdBasket = createBasket(request);
            //kafkaTemplate.send(CREATE_TOPIC, JSON.toJSONString(createdBasket, false));
            return basketRepository.save(createdBasket);
        }

        Basket basket = optionalBasket.get();

        if (!compareAddItemRequestWithBasket(request, basket)) {
            throw new RequestMismatchedWithBasketDataException();
        }

        if (basket.getItems().containsKey(request.getProductId())) {
            BasketItem item = getItemFromBasket(basket, request.getProductId());

            if (!compareAddItemRequestWithItem(request, item)) {
                throw new RequestMismatchedWithBasketDataException();
            }

            ItemRequest itemRequest = ItemRequest.builder()
                    .basketId(request.getBasketId())
                    .productId(request.getProductId())
                    .build();
            return increaseQuantity(itemRequest);
        }

        BasketItem item = new BasketItem();
        mapRequestToItem(item,request);

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
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }
        BasketItem item = getItemFromBasket(basket, request.getProductId());

        basket.removeItem(item);
        //kafkaTemplate.send(UPDATE_TOPIC, JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    @Override
    public Basket checkOrUncheckItem(ItemRequest request) {
        Optional<Basket> optionalBasket = basketRepository.findById(request.getBasketId());
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        if (!isItemInTheBasket(basket, request.getProductId())) {
            throw new ItemNotFoundException();
        }

        BasketItem item = getItemFromBasket(basket, request.getProductId());
        basket.reverseBasketItemStatus(item);
        return basketRepository.save(basket);
    }

    @Override
    public Basket completeOrder(Long basketId) {
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if (optionalBasket.isEmpty()) {
            throw new BasketNotFoundException();
        }
        if (BasketStatus.ORDERED.equals(optionalBasket.get().getStatus())) {
            throw new BasketAlreadyOrderedException();
        }
        Basket basket = optionalBasket.get();
        basket.changeBasketStatus();
        //kafkaTemplate.send(ORDER_TOPIC, JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    // *********************************************************************

    private Basket createBasket(AddItemRequest request) {
        Basket basket = new Basket();
        mapRequestToBasket(basket,request);
        BasketItem item = new BasketItem();
        mapRequestToItem(item,request);
        BasketInfo info = new BasketInfo();

        basket.setInfo(info);
        basket.addItem(item);
        return basket;
    }

    private void mapRequestToBasket(Basket basket, AddItemRequest request){
        basket.setId(request.getBasketId());
        basket.setSessionId(request.getSessionId());
        basket.setItems(new HashMap<>());
        basket.setStatus(BasketStatus.PENDING);
        Date date = new Date();
        basket.setCreationDate(date.getTime());
    }

    private void mapRequestToItem(BasketItem item, AddItemRequest request){
        item.setProductId(request.getProductId());
        item.setQuantity(DEFAULT_STARTING_QUANTITY);
        item.setProductPrice(request.getProductPrice());
        item.setProductImage(request.getProductImage());
        item.setProductInfo(request.getProductInfo());
        item.setStatus(BasketItemStatus.CHECKED);
    }

    private BasketItem getItemFromBasket(Basket basket, Long productId) {
        return basket.getItems().get(productId);
    }

    private boolean isItemInTheBasket(Basket basket, Long productId) {
        if (basket.getItems().get(productId) != null) {
            return true;
        }
        return false;
    }

    private boolean compareAddItemRequestWithBasket(AddItemRequest request, Basket basket) {
        if (!basket.getId().equals(request.getBasketId())) {
            return false;
        }
        if (!basket.getSessionId().equals(request.getSessionId())) {
            return false;
        }
        return true;
    }

    private boolean compareAddItemRequestWithItem(AddItemRequest request, BasketItem item) {
        if (!item.getProductPrice().equals(request.getProductPrice())) {
            return false;
        }
        if (!item.getProductImage().equals(request.getProductImage())) {
            return false;
        }
        if (!item.getProductInfo().equals(request.getProductInfo())) {
            return false;
        }
        return true;
    }
}
