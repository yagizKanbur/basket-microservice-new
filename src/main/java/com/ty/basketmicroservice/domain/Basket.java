package com.ty.basketmicroservice.domain;

import com.ty.basketmicroservice.enums.BasketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.Date;
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
    private Map<Long, BasketItem> items;
    @Field
    private BasketInfo info;
    @Field
    private Date creationDate;
    @Field
    private BasketStatus status;

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

    public Map<Long, BasketItem> getItems() {
        return items;
    }

    public void setItems(Map<Long, BasketItem> items) {
        this.items = items;
    }

    public BasketInfo getInfo() {
        return info;
    }

    public void setInfo(BasketInfo info) {
        this.info = info;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BasketStatus getStatus() {
        return status;
    }

    public void setStatus(BasketStatus status) {
        this.status = status;
    }
}
