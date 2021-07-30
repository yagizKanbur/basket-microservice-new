package com.ty.basketmicroservice.dto;

import java.util.UUID;

public class ChangeQuantityRequest {
    private Long basketId;
    private Long sessionId;
    private Long productId;
    private int quantity;

    public ChangeQuantityRequest(){

    }

    public ChangeQuantityRequest(AddItemRequest request){
        this.basketId = request.getBasketId();
        this.sessionId = request.getSessionId();
        this.productId = request.getProductId();
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


