package com.ty.basketmicroservice.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasketInfoTest {
    private static final Double GENERIC_PRICE = 20.00;


    @Test
    void calculatePrice_shouldBeSuccess(){
        BasketInfo info = new BasketInfo();
        BasketItem item = new BasketItem();
        item.setProductPrice(GENERIC_PRICE);
        info.calculatePrice(item);
        Double newPrice = GENERIC_PRICE + info.getSumOfShippingPrices();
        assertEquals(newPrice, info.getTotalPrice());
    }

    @Test
    void calculatePriceAfterDecreaseInQuantity_shouldBeSuccess(){
        BasketInfo info = new BasketInfo();
        BasketItem item = new BasketItem();
        item.setProductPrice(GENERIC_PRICE);
        info.setSumOfProductPrices(GENERIC_PRICE);
        info.calculatePriceAfterDecreaseInQuantity(item);
        Double newPrice = info.getSumOfProductPrices() + info.getSumOfShippingPrices();
        assertEquals(newPrice, info.getTotalPrice());
    }

    @Test
    void calculatePriceAfterChangingQuantity_shouldBeSuccess(){

    }
}