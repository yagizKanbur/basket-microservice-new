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

    public BasketItem(AddItemRequest request) {
        this.productId = request.getProductId();
        this.productImage = request.getProductImage();
        this.productInfo = request.getProductInfo();
        this.productPrice = request.getProductPrice();
        this.quantity = 1;
        this.status = BasketItemStatus.CHECKED;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        this.quantity--;
    }

    public void reverseBasketItemStatus() {
        if (this.status == BasketItemStatus.CHECKED) {
            this.status = BasketItemStatus.UNCHECKED;
        } else {
            this.status = BasketItemStatus.CHECKED;
        }
    }

}
