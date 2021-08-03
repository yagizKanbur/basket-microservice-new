package com.ty.basketmicroservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemRequest {
    private String basketId;
    private Long productId;

    @Override
    public String toString() {
        return "{" +
                "basketId=\"" + basketId + '\"' +
                ", productId=" + productId +
                '}';
    }
}
