package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class BasketAlreadyOrderedException extends BasketException{
    public BasketAlreadyOrderedException(){
        super(HttpStatus.BAD_REQUEST,"This basket is already ordered");
    }
}
