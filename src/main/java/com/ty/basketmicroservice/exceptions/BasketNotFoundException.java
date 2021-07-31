package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class BasketNotFoundException extends BasketException{
    public BasketNotFoundException(){
        super(HttpStatus.BAD_REQUEST,"Basket not found");
    }
}
