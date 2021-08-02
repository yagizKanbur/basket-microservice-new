package com.ty.basketmicroservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddItemRequest {
    private Long basketId;
    private Long sessionId;
    private Long productId;
    private String productImage;
    private String productInfo;
    private Double productPrice;

    @Override
    public String toString() {
        return "AddItemRequest{" +
                "basketId=" + basketId +
                ", sessionId=" + sessionId +
                ", productId=" + productId +
                ", productImage='" + productImage + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
