package com.ty.basketmicroservice.exceptions;

import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.repository.BasketRepository;
import com.ty.basketmicroservice.service.BasketService;
import com.ty.basketmicroservice.service.BasketServiceV1;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ExceptionTest {

    @Before
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    BasketServiceV1 service;

    @Test
    void throwItemNotFoundException_shouldBeSuccess() {
        Mockito.when(service.increaseQuantity(any())).thenThrow(ItemNotFoundException.class);
        Exception exception = assertThrows(ItemNotFoundException.class, () -> service.increaseQuantity(any()));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void throwBasketNotFoundException_shouldBeSuccess() {
        Mockito.when(service.increaseQuantity(any())).thenThrow(BasketNotFoundException.class);
        Exception exception = assertThrows(BasketNotFoundException.class, () -> service.increaseQuantity(any()));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void throwBasketAlreadyOrderedException_shouldBeSuccess() {
        Mockito.when(service.increaseQuantity(any())).thenThrow(BasketAlreadyOrderedException.class);
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> service.increaseQuantity(any()));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }
    @Test
    void throwNegativeQuantityException_shouldBeSuccess() {
        Mockito.when(service.increaseQuantity(any())).thenThrow(NegativeQuantityException.class);
        Exception exception = assertThrows(NegativeQuantityException.class, () -> service.increaseQuantity(any()));
        assertEquals(NegativeQuantityException.class, exception.getClass());
    }
    @Test
    void throwRequestMismatchedWithBasketDataException_shouldBeSuccess() {
        Mockito.when(service.increaseQuantity(any())).thenThrow(RequestMismatchedWithBasketDataException.class);
        Exception exception = assertThrows(RequestMismatchedWithBasketDataException.class, () -> service.increaseQuantity(any()));
        assertEquals(RequestMismatchedWithBasketDataException.class, exception.getClass());
    }
}
