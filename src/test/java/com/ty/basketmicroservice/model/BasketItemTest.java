package com.ty.basketmicroservice.model;

import com.ty.basketmicroservice.enums.BasketItemStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasketItemTest {
    private static final int QUANTITY = 2;

    @Test
    void increaseQuantity_shouldBeSuccess(){
        BasketItem item = new BasketItem();
        item.setQuantity(QUANTITY);
        item.increaseQuantity();
        assertEquals(QUANTITY+1,item.getQuantity());
    }

    @Test
    void decreaseQuantity_shouldBeSuccess(){
        BasketItem item = new BasketItem();
        item.setQuantity(QUANTITY);
        item.decreaseQuantity();
        assertEquals(QUANTITY-1,item.getQuantity());
    }

    @Test
    void reverseBasketStatusFromCheckedToUnchecked_shouldBeSuccess(){
        BasketItem item = new BasketItem();
        item.setStatus(BasketItemStatus.CHECKED);
        item.reverseBasketItemStatus();
        assertEquals(BasketItemStatus.UNCHECKED, item.getStatus());
    }

    @Test
    void reverseBasketStatusFromUncheckedToChecked_shouldBeSuccess(){
        BasketItem item = new BasketItem();
        item.setStatus(BasketItemStatus.UNCHECKED);
        item.reverseBasketItemStatus();
        assertEquals(BasketItemStatus.CHECKED, item.getStatus());
    }

}