package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class RequestMismatchedWithBasketDataException extends BasketException {
    public RequestMismatchedWithBasketDataException(){
        super(HttpStatus.BAD_REQUEST, "Request mismatched with existing data");
    }
}
