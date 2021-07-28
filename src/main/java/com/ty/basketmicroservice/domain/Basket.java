package com.ty.basketmicroservice.domain;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import com.ty.basketmicroservice.enums.BasketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private UUID id;
    @Field
    private UUID sessionId;
    @Field
    private Map<UUID, BasketItem> items;
    @Field
    private BasketInfo info;
    @Field
    private Long creationDate;
    @Field
    private BasketStatus status;

    public Basket() {
    }

    public Basket(AddItemRequest request) {
        this.sessionId = request.getSessionId();
        this.items = new HashMap<>();
        this.info = new BasketInfo();
        this.status = BasketStatus.PENDING;
        Date date = new Date();
        this.creationDate = date.getTime();
    }

    public void changeBasketStatus(){
        if(this.status == BasketStatus.PENDING){
            this.status = BasketStatus.ORDERED;
        }
        else if(this.status == BasketStatus.ORDERED){
            this.status = BasketStatus.PENDING;
        }
    }

    public void addItem(BasketItem item){
        items.putIfAbsent(item.getProductId(), item);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Map<UUID, BasketItem> getItems() {
        return items;
    }

    public void setItems(Map<UUID, BasketItem> items) {
        this.items = items;
    }

    public BasketInfo getInfo() {
        return info;
    }

    public void setInfo(BasketInfo info) {
        this.info = info;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public BasketStatus getStatus() {
        return status;
    }

    public void setStatus(BasketStatus status) {
        this.status = status;
    }
}
