package com.ty.basketmicroservice.advice;

import com.ty.basketmicroservice.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class BasketControllerAdvice {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleNotSupportedMethod(HttpRequestMethodNotSupportedException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BasketException.class)
    public ResponseEntity<String> handleBasketException (BasketException e){
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BasketNotFoundException.class) //
    public ResponseEntity<?> handleBasketNotFound(BasketNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(),e.getHttpStatus());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFound(ItemNotFoundException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(),e.getHttpStatus());
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<String> handleKafkaException(KafkaException e) {
        log.info(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //  Handling basket exception now

    /*
    @ExceptionHandler(RequestMismatchedWithBasketDataException.class)
    public ResponseEntity<String> handleRequestMismatchedWithBasketData (RequestMismatchedWithBasketDataException e){
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativeQuantityException.class)
    public ResponseEntity<String> handleNegativeQuantityException (NegativeQuantityException e){
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativePriceException.class)
    public ResponseEntity<String> handleNegativePriceException (NegativePriceException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    } */


}
