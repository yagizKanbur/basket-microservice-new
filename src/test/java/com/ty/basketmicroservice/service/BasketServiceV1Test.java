package com.ty.basketmicroservice.service;

import com.ty.basketmicroservice.dto.ChangeQuantityRequest;
import com.ty.basketmicroservice.dto.ItemRequest;
import com.ty.basketmicroservice.enums.BasketStatus;
import com.ty.basketmicroservice.exceptions.BasketAlreadyOrderedException;
import com.ty.basketmicroservice.exceptions.BasketNotFoundException;
import com.ty.basketmicroservice.exceptions.ItemNotFoundException;
import com.ty.basketmicroservice.exceptions.NegativeQuantityException;
import com.ty.basketmicroservice.model.Basket;
import com.ty.basketmicroservice.model.BasketInfo;
import com.ty.basketmicroservice.model.BasketItem;
import com.ty.basketmicroservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

@ExtendWith(MockitoExtension.class)
class BasketServiceV1Test {

    private static final Long BASKET_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final int QUANTITY = 1;

    @Before
    public void createMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketServiceV1 basketService;

    //************************** Increase Quantity **************************************

    @Test
    void increaseQuantity_emptyBasketGiven_shouldThrowBasketNotFound(){
        ItemRequest itemRequest = ItemRequest.builder().build();
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.increaseQuantity(itemRequest));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void increaseQuantity_orderedBasketGiven_shouldThrowBasketAlreadyOrdered(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.increaseQuantity(itemRequest));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void increaseQuantity_notExistingItemGiven_shouldThrowItemNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(ItemNotFoundException.class, () -> basketService.increaseQuantity(itemRequest));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void increaseQuantity_validInputGiven_shouldIncreaseQuantity(){
        ItemRequest request = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);
        basket.setItems(new HashMap<>());

        BasketInfo info = new BasketInfo();
        info.setSumOfProductPrices(4.00);

        basket.setInfo(info);
        BasketItem item = new BasketItem();
        item.setProductId(PRODUCT_ID);
        item.setProductPrice(4.00);
        item.setQuantity(QUANTITY);
        basket.getItems().putIfAbsent(item.getProductId(),item);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket newBasket = basketService.increaseQuantity(request);

        assertEquals(QUANTITY + 1, newBasket.getItems().get(PRODUCT_ID).getQuantity());
    }

    //************************** Decrease Quantity **************************************

    @Test
    void decreaseQuantity_emptyBasketGiven_shouldThrowBasketNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().build();
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.decreaseQuantity(itemRequest));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void decreaseQuantity_orderedItemGiven_shouldThrowBasketAlreadyOrdered(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.decreaseQuantity(itemRequest));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void decreaseQuantity_notExistingItemGiven_shouldThrowItemNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(ItemNotFoundException.class, () -> basketService.decreaseQuantity(itemRequest));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void decreaseQuantity_validInputGiven_shouldDecreaseQuantity(){
        ItemRequest request = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);
        basket.setItems(new HashMap<>());

        BasketInfo info = new BasketInfo();
        info.setSumOfProductPrices(4.00);

        basket.setInfo(info);
        BasketItem item = new BasketItem();
        item.setProductId(PRODUCT_ID);
        item.setProductPrice(4.00);
        item.setQuantity(QUANTITY+2);
        basket.getItems().putIfAbsent(item.getProductId(),item);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket newBasket = basketService.decreaseQuantity(request);

        assertEquals(QUANTITY + 1, newBasket.getItems().get(PRODUCT_ID).getQuantity());
    }

    //************************** Remove Item **************************************

    @Test
    void removeItem_emptyBasketGiven_shouldThrowBasketNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().build();
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.removeItem(itemRequest));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void removeItem_orderedItemGiven_shouldThrowBasketAlreadyOrdered(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.removeItem(itemRequest));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void removeItem_notExistingItemGiven_shouldThrowItemNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(ItemNotFoundException.class, () -> basketService.removeItem(itemRequest));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void removeItem_validInputGiven_shouldRemoveItem(){
        ItemRequest request = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);
        BasketInfo info = new BasketInfo();
        info.setSumOfProductPrices(4.00);

        basket.setInfo(info);
        BasketItem item = new BasketItem();
        item.setProductId(PRODUCT_ID);
        item.setProductPrice(4.00);
        item.setQuantity(QUANTITY+2);
        basket.getItems().putIfAbsent(PRODUCT_ID,item);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket newBasket = basketService.removeItem(request);

        assertEquals(basket, newBasket);
    }

    //************************** Check Or Uncheck Item **************************************

    @Test
    void checkOrUncheckItem_emptyBasketGiven_shouldThrowBasketNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().build();
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.checkOrUncheckItem(itemRequest));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void checkOrUncheckItem_orderedItemGiven_shouldThrowBasketAlreadyOrdered(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.checkOrUncheckItem(itemRequest));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void checkOrUncheckItem_notExistingItemGiven_shouldThrowItemNotFoundException(){
        ItemRequest itemRequest = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(ItemNotFoundException.class, () -> basketService.checkOrUncheckItem(itemRequest));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void checkOrUncheckItem_validInputGiven_shouldRemoveItem(){
        ItemRequest request = ItemRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);
        BasketInfo info = new BasketInfo();
        info.setSumOfProductPrices(4.00);

        basket.setInfo(info);
        BasketItem item = new BasketItem();
        item.setProductId(PRODUCT_ID);
        item.setProductPrice(4.00);
        item.setQuantity(QUANTITY+2);
        basket.getItems().putIfAbsent(PRODUCT_ID,item);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket newBasket = basketService.checkOrUncheckItem(request);

        assertEquals(basket, newBasket);
    }

    //************************** Change Quantity **************************************

    @Test
    void changeQuantity_negativeQuantityGiven_shouldThrowNegativeQuantity(){
        ChangeQuantityRequest request = ChangeQuantityRequest.builder().quantity(-100).build();
        Exception exception = assertThrows(NegativeQuantityException.class, () -> basketService.changeQuantity(request));
        assertEquals(NegativeQuantityException.class, exception.getClass());
    }

    @Test
    void changeQuantity_emptyBasketGiven_shouldThrowBasketNotFoundException(){
        ChangeQuantityRequest request = ChangeQuantityRequest.builder().quantity(QUANTITY).build();
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.changeQuantity(request));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void changeQuantity_orderedItemGiven_shouldThrowBasketAlreadyOrdered(){
        ChangeQuantityRequest request = ChangeQuantityRequest.builder().quantity(QUANTITY).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.changeQuantity(request));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void changeQuantity_notExistingItemGiven_shouldThrowItemNotFoundException(){
        ChangeQuantityRequest request = ChangeQuantityRequest.builder().quantity(QUANTITY).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(ItemNotFoundException.class, () -> basketService.changeQuantity(request));
        assertEquals(ItemNotFoundException.class, exception.getClass());
    }

    @Test
    void changeQuantity_validInputGiven_shouldChangeQuantity(){
    ChangeQuantityRequest request = ChangeQuantityRequest.builder().basketId(BASKET_ID).productId(PRODUCT_ID).quantity(QUANTITY).build();

        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);
        basket.setItems(new HashMap<>());

        BasketInfo info = new BasketInfo();
        info.setSumOfProductPrices(4.00);

        basket.setInfo(info);
        BasketItem item = new BasketItem();
        item.setProductId(PRODUCT_ID);
        item.setProductPrice(4.00);
        basket.getItems().putIfAbsent(item.getProductId(),item);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket newBasket = basketService.changeQuantity(request);

        assertEquals( request.getQuantity(), newBasket.getItems().get(PRODUCT_ID).getQuantity());
    }

    //************************** Add Item ********************************************



    //************************** Complete Order **************************************


    @Test
    void completeOrder_emptyBasketGiven_shouldThrowBasketNotFoundException(){
        Exception exception = assertThrows(BasketNotFoundException.class, () -> basketService.completeOrder(BASKET_ID));
        assertEquals(BasketNotFoundException.class, exception.getClass());
    }

    @Test
    void completeOrder_orderedItemGiven_shouldThrowBasketAlreadyOrdered(){
        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.ORDERED);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Exception exception = assertThrows(BasketAlreadyOrderedException.class, () -> basketService.completeOrder(BASKET_ID));
        assertEquals(BasketAlreadyOrderedException.class, exception.getClass());
    }

    @Test
    void completeOrder_validInputGiven_shouldChangeStatusToOrdered(){
        Basket basket = new Basket();
        basket.setId(BASKET_ID);
        basket.setStatus(BasketStatus.PENDING);

        Mockito.when(basketRepository.findById(any())).thenReturn(java.util.Optional.of(basket));
        Mockito.when(basketRepository.save(any())).thenReturn(basket);
        Basket orderedBasket = basketService.completeOrder(BASKET_ID);

        assertEquals(BasketStatus.ORDERED,orderedBasket.getStatus());
    }



}