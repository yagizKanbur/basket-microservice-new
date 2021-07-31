package com.ty.basketmicroservice.advice;

import com.ty.basketmicroservice.exceptions.BasketNotFoundException;
import com.ty.basketmicroservice.exceptions.ItemNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@Slf4j
@ControllerAdvice
public class BasketControllerAdvice {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleNotSupportedMethod (HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException){
        //log.ınfo()
        return ResponseEntity.badRequest().body("Request is not valid");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument (IllegalArgumentException illegalArgumentException){
        return  ResponseEntity.badRequest().body("The given id must not be null");
    }

    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<String> handleBasketNotFound (BasketNotFoundException e){
        return  ResponseEntity.badRequest().body("Basket not found");
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFound (ItemNotFoundException e){
        return ResponseEntity.badRequest().body("Item not found in the basket");
    }
}
