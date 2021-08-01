package com.ty.basketmicroservice.advice;

import com.ty.basketmicroservice.exceptions.BasketNotFoundException;
import com.ty.basketmicroservice.exceptions.ItemNotFoundException;
import com.ty.basketmicroservice.exceptions.NegativeQuantityException;
import com.ty.basketmicroservice.exceptions.RequestMismatchedWithBasketDataException;
import org.apache.kafka.common.KafkaException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@Slf4j
@ControllerAdvice
public class BasketControllerAdvice {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleNotSupportedMethod(HttpRequestMethodNotSupportedException e) {
        //log.ınfo()
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<String> handleBasketNotFound(BasketNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<String> handleKafkaException(KafkaException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RequestMismatchedWithBasketDataException.class)
    public ResponseEntity<String> handleRequestMismatchedWithBasketData (RequestMismatchedWithBasketDataException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NegativeQuantityException.class)
    public ResponseEntity<String> handleNegativeQuantityException (NegativeQuantityException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
