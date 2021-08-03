package com.ty.basketmicroservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangeQuantityRequest {
    private String basketId;
    private Long productId;
    private int quantity;


}


