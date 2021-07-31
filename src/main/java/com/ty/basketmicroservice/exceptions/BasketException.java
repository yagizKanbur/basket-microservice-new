package com.ty.basketmicroservice.exceptions;

import org.springframework.http.HttpStatus;

public class BasketException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;

    public BasketException(){
        super();
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        message = "Something happened";
    }

    public BasketException(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
