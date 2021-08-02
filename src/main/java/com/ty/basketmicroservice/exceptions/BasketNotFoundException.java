package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class BasketNotFoundException extends BasketException{
    public BasketNotFoundException(){
        super(HttpStatus.NOT_FOUND,"Basket not found");
    }
}
