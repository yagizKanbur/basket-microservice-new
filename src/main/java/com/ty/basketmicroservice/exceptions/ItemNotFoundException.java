package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends BasketException {
    public ItemNotFoundException(){
        super(HttpStatus.NOT_FOUND ,"product not found in the basket");
    }
}
