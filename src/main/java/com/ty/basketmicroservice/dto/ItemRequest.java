package com.ty.basketmicroservice.dto;

import java.util.UUID;

public class ItemRequest {
    private UUID sessionId;
    private UUID productId;

    public ItemRequest(){

    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
