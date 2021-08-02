package com.ty.basketmicroservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
@Getter
@Setter
public class BasketInfo {
    // Todo: Should we implement id here.
    @Field
    private Double sumOfProductPrices;
    @Field
    private Double sumOfShippingPrices;
    @Field
    private Double totalPrice;
    @Field
    private boolean isFreeShipping;

    public BasketInfo(){
        sumOfShippingPrices= 11.99;
        isFreeShipping = false;
        totalPrice = sumOfShippingPrices;
    }

    public void setTotalPrice() {
        this.totalPrice = sumOfProductPrices +  sumOfShippingPrices;
    }

    //*************************************************************************

    public void calculatePrice(BasketItem item) {
        if (this.getSumOfProductPrices() == null) {
            this.setSumOfProductPrices(item.getProductPrice());
        } else {
            Double newPrice = item.getProductPrice() + this.getSumOfProductPrices();
            this.setSumOfProductPrices(newPrice);
        }
        this.setTotalPrice();

        if(this.getSumOfProductPrices()>60 && !this.isFreeShipping()){
            makeShippingFree();
        }
    }

    public void makeShippingFree(){
        this.setSumOfShippingPrices(this.getSumOfShippingPrices()-11.99);
        this.setFreeShipping(true);
        this.setTotalPrice();
    }

}
