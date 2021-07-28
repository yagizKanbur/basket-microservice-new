package com.ty.basketmicroservice.dto;

import java.util.UUID;

public class ChangeQuantityRequest {
    private UUID sessionId;
    private UUID productId;
    private int quantity;

    public ChangeQuantityRequest(){

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


