package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends BasketException {
    public ItemNotFoundException(){
        super(HttpStatus.BAD_REQUEST ,"product not found");
    }
}
