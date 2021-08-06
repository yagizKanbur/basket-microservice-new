package com.ty.basketmicroservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemEvent {
    private String basketId;
    private Long userId;
    private Long productId;
}
