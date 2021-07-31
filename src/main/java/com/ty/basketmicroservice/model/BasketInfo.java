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

    public BasketInfo(){
        sumOfShippingPrices= 4.0;
        totalPrice = sumOfShippingPrices;
    }

    public void setTotalPrice() {
        this.totalPrice = sumOfProductPrices +  sumOfShippingPrices;
    }
}
