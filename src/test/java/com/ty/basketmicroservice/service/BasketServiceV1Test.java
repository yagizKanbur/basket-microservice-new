package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.domain.Basket;
import com.ty.basketmicroservice.repository.BasketRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceV1Test {

    @Before
    public void createMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketServiceV1 basketService;

    @Test
    void addItemToBasket_givenValidRequest_shouldSaveAndReturnBasket(){
    }

}