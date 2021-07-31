package com.ty.basketmicroservice.service;

import com.alibaba.fastjson.JSON;
import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.domain.BasketInfo;
import com.ty.basketmicroservice.domain.BasketItem;
import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import com.ty.basketmicroservice.enums.BasketStatus;
import com.ty.basketmicroservice.exceptions.ItemNotFoundException;
import com.ty.basketmicroservice.repository.BasketRepository;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BasketServiceV1 implements BasketService {
    private static final String UPDATE_TOPIC = "basket.update";
    private static final String CREATE_TOPIC = "basket.create";
    private static final String DELETE_TOPIC = "basket.delete";


    private final BasketRepository basketRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BasketServiceV1(BasketRepository basketRepository, KafkaTemplate<String, String> kafkaTemplate){
        this.basketRepository = basketRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public Basket increaseQuantity(ChangeQuantityRequest request) {
        Long productId = request.getProductId();
        Basket basket = getBasketOrElseThrow(request.getBasketId());

        if(isItemInTheBasket(basket, productId)){
            BasketItem item = getItemFromBasket(basket, productId);
            item.increaseQuantity();

            BasketInfo info = calculatePrice(basket.getInfo(),item);
            basket.setInfo(info);


            return basketRepository.save(basket);
        }

        return basket;
    }

    @Override
     public Basket decreaseQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        BasketItem item = getItemFromBasket(basket, request.getProductId());

        if(!isItemInTheBasket(basket,request.getProductId())){
            return basket;
        }

        if(item.getQuantity() == 1){
            ItemRequest removeRequest = new ItemRequest(request);
            return removeItem(removeRequest);
        }

        item.decreaseQuantity();
        subtractProductPriceFromTotalPrice(basket.getInfo(), item);

        return basketRepository.save(basket);
    }

    @Override
    public Basket changeQuantity(ChangeQuantityRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        if(!isItemInTheBasket(basket,request.getProductId())){
            return basket;
        }

        BasketItem item = getItemFromBasket(basket,request.getProductId());
        int changeInQuantity = request.getQuantity() - item.getQuantity();
        item.setQuantity(request.getQuantity());
        calculatePriceAfterChangingQuantity(basket.getInfo(), item, changeInQuantity);

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
            kafkaTemplate.send(CREATE_TOPIC,  JSON.toJSONString(basket, false));
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
        kafkaTemplate.send(UPDATE_TOPIC,  JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    @Override
    public Basket removeItem(ItemRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        if(!isItemInTheBasket(basket,request.getProductId())){
            return basket;
        }

        BasketItem item = getItemFromBasket(basket,request.getProductId());

        basket.getItems().remove(item.getProductId());

        calculatePriceAfterRemovingItem(basket.getInfo(), item);
        kafkaTemplate.send(UPDATE_TOPIC,  JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    @Override
    public Basket checkOrUncheckItem(ItemRequest request) {
        Basket basket = getBasketOrElseThrow(request.getBasketId());
        if(!isItemInTheBasket(basket,request.getProductId())){
            return basket;
        }

        BasketItem item = getItemFromBasket(basket,request.getProductId());
        item.changeCheckBox();
        calculatePriceAfterCheckboxChange(basket.getInfo(), item, item.getStatus());

        return basketRepository.save(basket);
    }

    @Override
    public Basket completeOrder(Long basketId) {
        Basket basket = getBasketOrElseThrow(basketId);
        basket.changeBasketStatus();
        kafkaTemplate.send(UPDATE_TOPIC,  JSON.toJSONString(basket, false));
        return basketRepository.save(basket);
    }

    // *********************************************************************

    public Basket getBasketOrElseThrow(Long id){
        return basketRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public BasketItem getItemFromBasket(Basket basket, Long id){
        return basket.getItems().get(id);
    }

    public BasketStatus checkBasketStatus (Long basketId){
        Basket basket = getBasketOrElseThrow(basketId);
        return basket.getStatus();
    }

    public boolean isItemInTheBasket (Basket basket, Long productId) throws NullPointerException{
        if (basket.getItems().get(productId) != null){
            return true;
        }
        return false;
    }

    // **************** Price Calculation Methods **************************

    public BasketInfo calculatePrice(BasketInfo info ,BasketItem item){
        if (info.getSumOfProductPrices() == null){
            info.setSumOfProductPrices(item.getProductPrice());
            info.setTotalPrice();
            return info;
        }
        Double newPrice = item.getProductPrice() + info.getSumOfProductPrices();
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
        return info;
    }

    public void subtractProductPriceFromTotalPrice(BasketInfo info, BasketItem item){
        Double newPrice = info.getSumOfProductPrices() - item.getProductPrice();
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
    }

    public void calculatePriceAfterRemovingItem (BasketInfo info, BasketItem item){
        Double newPrice = info.getSumOfProductPrices() - item.getProductPrice()*item.getQuantity();
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
    }

    public void calculatePriceAfterChangingQuantity(BasketInfo info, BasketItem item, int changeInQuantity){
        Double newPrice = info.getSumOfProductPrices() + item.getProductPrice() * changeInQuantity;
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
    }

    public void calculatePriceAfterCheckboxChange(BasketInfo info, BasketItem item , BasketItemStatus status ){
        if(status.equals(BasketItemStatus.CHECKED)){
            Double newPrice = info.getSumOfProductPrices() + item.getProductPrice() * item.getQuantity();
            info.setSumOfProductPrices(newPrice);
            info.setTotalPrice();
        }else if(status.equals(BasketItemStatus.UNCHECKED)){
            Double newPrice = info.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
            info.setSumOfProductPrices(newPrice);
            info.setTotalPrice();
        }
    }
}
