package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class NegativePriceException extends BasketException{
    public NegativePriceException(){
        super(HttpStatus.BAD_REQUEST, "Price cannot be zero or a negative value");
    }
}
