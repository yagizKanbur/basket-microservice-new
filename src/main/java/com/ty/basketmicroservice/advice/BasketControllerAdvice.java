package com.ty.basketmicroservice.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BasketControllerAdvice {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleNotSupportedMethod (HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        return ResponseEntity.badRequest().body("Request is not valid");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument (IllegalArgumentException illegalArgumentException){
        return  ResponseEntity.badRequest().body("The given id must not be null");
    }
}
