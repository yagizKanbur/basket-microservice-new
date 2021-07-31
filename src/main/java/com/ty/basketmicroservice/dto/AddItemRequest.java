package com.ty.basketmicroservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {
    private Long basketId;
    private Long sessionId;
    private Long productId;
    private String productImage;
    private String productInfo;
    private Double productPrice;

    public AddItemRequest() {
    }
}
