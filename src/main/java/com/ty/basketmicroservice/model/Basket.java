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
    private String basketId;
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
        this.info.calculatePrice(item);
    }

    public void removeItem(BasketItem item) {
        this.getItems().remove(item.getProductId());
        this.info.calculatePriceAfterRemovingItem(item);
    }

    public void increaseItemQuantity(BasketItem item) {
        item.increaseQuantity();
        if(BasketItemStatus.CHECKED.equals(item.getStatus())){
            this.info.calculatePrice(item);
        }
    }

    public void decreaseItemQuantity(BasketItem item) {
        item.decreaseQuantity();
        if(BasketItemStatus.CHECKED.equals(item.getStatus())){
            this.info.calculatePriceAfterDecreaseInQuantity(item);
        }
    }

    public void changeItemQuantity(BasketItem item, int newQuantity) {
        int changeInQuantity = newQuantity - item.getQuantity();
        item.setQuantity(newQuantity);
        if(BasketItemStatus.CHECKED.equals(item.getStatus())){
            this.info.calculatePriceAfterChangingQuantity(item, changeInQuantity);
        }
    }

    public void reverseBasketItemStatus(BasketItem item) {
        item.reverseBasketItemStatus();
        this.info.calculatePriceAfterChangingCheckboxStatus(item, item.getStatus());
    }

}
