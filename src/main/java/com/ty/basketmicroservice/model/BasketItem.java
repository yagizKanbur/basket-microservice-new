package com.ty.basketmicroservice.model;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
@Getter
@Setter
public class BasketItem {
    @Id
    private Long productId;

    @Field
    private String productImage;

    @Field
    private String productInfo;

    @Field
    private Double productPrice;

    @Field
    private int quantity;

    @Field
    private BasketItemStatus status;

    public BasketItem() {
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    public void reverseBasketItemStatus() { // Todo : set status with set status method
        if (this.status == BasketItemStatus.CHECKED) {
            this.status = BasketItemStatus.UNCHECKED;
        } else {
            this.status = BasketItemStatus.CHECKED;
        }
    }

}
