package com.ty.basketmicroservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddItemRequest {
    private String basketId;
    private Long userId;
    private Long productId;
    private String productImage;
    private String productInfo;
    private Double productPrice;

    @Override
    public String toString() {
        return "AddItemRequest{" +
                "basketId='" + basketId + '\'' +
                ", sessionId=" + userId +
                ", productId=" + productId +
                ", productImage='" + productImage + '\'' +
                ", productInfo='" + productInfo + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
