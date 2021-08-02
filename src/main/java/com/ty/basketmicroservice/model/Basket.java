package com.ty.basketmicroservice.model;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import com.ty.basketmicroservice.enums.BasketStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.*;

@Document
@Getter
@Setter
public class Basket {
    @Id @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private Long id;
    @Field
    private Long sessionId;
    @Field
    private Map<Long, BasketItem> items;
    @Field
    private BasketInfo info;
    @Field
    private Long creationDate;
    @Field
    private BasketStatus status;

    public Basket() {
        this.items = new HashMap<>();
        this.info = new BasketInfo();
        this.status = BasketStatus.PENDING;
        Date date = new Date();
        this.creationDate = date.getTime();
    }

    public Basket(AddItemRequest request) {
        this.id = request.getBasketId();
        this.sessionId = request.getSessionId();
        this.items = new HashMap<>();
        this.info = new BasketInfo();
        this.status = BasketStatus.PENDING;
        Date date = new Date();
        this.creationDate = date.getTime();
    }

    public Map<Long, BasketItem> getItems() {
        //Todo: return Collections.unmodifiableMap(items);
        return items;
    }

    public void changeBasketStatus() {
        if (this.status == BasketStatus.PENDING) {
            this.status = BasketStatus.ORDERED;
        } else if (this.status == BasketStatus.ORDERED) {
            this.status = BasketStatus.PENDING;
        }
    }

    public void addItem(BasketItem item) {
        this.getItems().putIfAbsent(item.getProductId(), item);
        this.calculatePrice(item);
    }

    public void removeItem(BasketItem item) {
        this.getItems().remove(item.getProductId());
        calculatePriceAfterRemovingItem(item);
    }

    public void increaseItemQuantity(BasketItem item) {
        item.increaseQuantity();
        this.info.calculatePrice(item);
    }

    public void decreaseItemQuantity(BasketItem item) {
        item.decreaseQuantity();
        calculatePriceAfterDecreaseInQuantity(item);
    }

    public void changeItemQuantity(BasketItem item, int newQuantity) {
        int changeInQuantity = newQuantity - item.getQuantity();
        item.setQuantity(newQuantity);
        calculatePriceAfterChangingQuantity(item, changeInQuantity);
    }

    public void reverseBasketItemStatus(BasketItem item) {
        item.reverseBasketItemStatus();
        calculatePriceAfterChangingCheckboxStatus(item, item.getStatus());
    }

    //****************** Price Calculation Methods *******************************

    public void calculatePrice(BasketItem item) {
        if (info.getSumOfProductPrices() == null) {
            info.setSumOfProductPrices(item.getProductPrice());
        } else {
            Double newPrice = item.getProductPrice() + info.getSumOfProductPrices();
            info.setSumOfProductPrices(newPrice);
        }
        info.setTotalPrice();

        if(info.getSumOfProductPrices()>60 && !info.isFreeShipping()){
            makeShippingFree();
        }
    }

    public void calculatePriceAfterDecreaseInQuantity(BasketItem item) {
        Double newPrice = info.getSumOfProductPrices() - item.getProductPrice();
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
        if(info.getSumOfProductPrices()<=60 && info.isFreeShipping()){
            makeShippingPaidFor();
        }
    }

    public void calculatePriceAfterChangingQuantity(BasketItem item, int changeInQuantity) {
        Double newPrice = info.getSumOfProductPrices() + item.getProductPrice() * changeInQuantity;
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
        if(info.getSumOfProductPrices()<=60 && info.isFreeShipping()){
            makeShippingPaidFor();
        }
        if(info.getSumOfProductPrices()>60 && !info.isFreeShipping()){
            makeShippingFree();
        }
    }

    public void calculatePriceAfterRemovingItem(BasketItem item) {
        Double newPrice = info.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
        info.setSumOfProductPrices(newPrice);
        info.setTotalPrice();
        if(info.getSumOfProductPrices()<=60 && info.isFreeShipping()){
            makeShippingPaidFor();
        }
    }

    public void calculatePriceAfterChangingCheckboxStatus(BasketItem item, BasketItemStatus status) {
        if (BasketItemStatus.CHECKED.equals(status)) {
            Double newPrice = info.getSumOfProductPrices() + item.getProductPrice() * item.getQuantity();
            info.setSumOfProductPrices(newPrice);
            info.setTotalPrice();
        } else if (BasketItemStatus.UNCHECKED.equals(status)) {
            Double newPrice = info.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
            info.setSumOfProductPrices(newPrice);
            info.setTotalPrice();
        }
        if(info.getSumOfProductPrices()<=60 && info.isFreeShipping()){
            makeShippingPaidFor();
        }
        if(info.getSumOfProductPrices()>60 && !info.isFreeShipping()){
            makeShippingFree();
        }
    }

    public void makeShippingFree(){
        info.setSumOfShippingPrices(info.getSumOfShippingPrices()-11.99);
        info.setFreeShipping(true);
        info.setTotalPrice();
    }

    public void makeShippingPaidFor(){
        info.setSumOfShippingPrices(info.getSumOfShippingPrices()+11.99);
        info.setFreeShipping(false);
        info.setTotalPrice();
    }


}
