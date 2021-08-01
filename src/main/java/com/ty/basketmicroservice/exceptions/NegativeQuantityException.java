package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class NegativeQuantityException extends BasketException{
    public NegativeQuantityException (){
        super(HttpStatus.BAD_REQUEST, "Quantity can't be '0' or a negative number");
    }
}
