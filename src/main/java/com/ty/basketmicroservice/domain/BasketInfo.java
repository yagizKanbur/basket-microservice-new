package com.ty.basketmicroservice.domain;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
public class BasketInfo {
    // Todo: Should we implement id here.
    @Field
    private Double sumOfProductPrices;
    @Field
    private Double sumOfShippingPrices;
    @Field
    private Double totalPrice;

    public BasketInfo(){
        sumOfShippingPrices= 4.0;
        totalPrice = sumOfShippingPrices;
    }

    public Double getSumOfProductPrices() {
        return sumOfProductPrices;
    }

    public void setSumOfProductPrices(Double sumOfProductPrices) {
        this.sumOfProductPrices = sumOfProductPrices;
    }

    public Double getSumOfShippingPrices() {
        return sumOfShippingPrices;
    }

    public void setSumOfShippingPrices(Double sumOfShippingPrices) {
        this.sumOfShippingPrices = sumOfShippingPrices;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice() {
        this.totalPrice = sumOfProductPrices +  sumOfShippingPrices;
    }
}
