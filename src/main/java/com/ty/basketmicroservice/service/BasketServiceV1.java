package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.repository.BasketRepository;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceV1 implements BasketService {
    private final BasketRepository basketRepository;

    public BasketServiceV1(BasketRepository basketRepository){
        this.basketRepository = basketRepository;
    }


}
