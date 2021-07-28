package com.ty.basketmicroservice.domain;

import com.ty.basketmicroservice.dto.AddItemRequest;
import com.ty.basketmicroservice.enums.BasketItemStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.UUID;

@Document
public class BasketItem {
     @Id
     private UUID productId;

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

    public BasketItem(AddItemRequest request){
        this.productId = request.getProductId();
        this.productImage = request.getProductImage();
        this.productInfo = request.getProductInfo();
        this.productPrice = request.getProductPrice();
        this.quantity = 1;
        this.status = BasketItemStatus.CHECKED;
    }

    public void increaseQuantity(){
        this.quantity++;
    }

    public void decreaseQuantity(){
        this.quantity--;
    }

    public void changeCheckBox(){
        if(this.status == BasketItemStatus.CHECKED){
            this.status = BasketItemStatus.UNCHECKED;
        }
        else if(this.status == BasketItemStatus.UNCHECKED){
            this.status = BasketItemStatus.CHECKED;
        }
    }

     public UUID getProductId() {
          return productId;
     }

     public void setProductId(UUID productId) {
          this.productId = productId;
     }

     public String getProductImage() {
          return productImage;
     }

     public void setProductImage(String productImage) {
          this.productImage = productImage;
     }

     public String getProductInfo() {
          return productInfo;
     }

     public void setProductInfo(String productInfo) {
          this.productInfo = productInfo;
     }

     public Double getProductPrice() {
          return productPrice;
     }

     public void setProductPrice(Double productPrice) {
          this.productPrice = productPrice;
     }

     public int getQuantity() {
          return quantity;
     }

     public void setQuantity(int quantity) {
          this.quantity = quantity;
     }

     public BasketItemStatus getStatus() {
          return status;
     }

     public void setStatus(BasketItemStatus status) {
          this.status = status;
     }
}
