package com.ty.basketmicroservice.model;

import com.ty.basketmicroservice.enums.BasketItemStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private Double shippingPrice;
    private Double shippingThreshold;

    public BasketInfo(Double shippingPrice, Double shippingThreshold) {
        this.shippingPrice = shippingPrice;
        this.shippingThreshold = shippingThreshold;
        this.sumOfShippingPrices = shippingPrice;
        isFreeShipping = false;
        totalPrice = sumOfShippingPrices;
    }

    public void setTotalPrice() {
        this.totalPrice = sumOfProductPrices + sumOfShippingPrices;
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

        if (this.getSumOfProductPrices() > shippingThreshold && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void calculatePriceAfterDecreaseInQuantity(BasketItem item) {
        Double newPrice = this.getSumOfProductPrices() - item.getProductPrice();
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= shippingThreshold && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
    }

    public void calculatePriceAfterChangingQuantity(BasketItem item, int changeInQuantity) {
        Double newPrice = this.getSumOfProductPrices() + item.getProductPrice() * changeInQuantity;
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= shippingThreshold && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
        if (this.getSumOfProductPrices() > shippingThreshold && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void calculatePriceAfterRemovingItem(BasketItem item) {
        Double newPrice = this.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= shippingThreshold && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
    }

    public void calculatePriceAfterChangingCheckboxStatus(BasketItem item, BasketItemStatus status) {
        if (BasketItemStatus.CHECKED.equals(status)) {
            Double newPrice = this.getSumOfProductPrices() + item.getProductPrice() * item.getQuantity();
            this.setSumOfProductPrices(newPrice);
            this.setTotalPrice();
        } else if (BasketItemStatus.UNCHECKED.equals(status)) {
            Double newPrice = this.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
            this.setSumOfProductPrices(newPrice);
            this.setTotalPrice();
        }
        if (this.getSumOfProductPrices() <= shippingThreshold && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
        if (this.getSumOfProductPrices() > shippingThreshold && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void makeShippingFree() {
        this.setSumOfShippingPrices(this.getSumOfShippingPrices() - shippingPrice);
        this.setFreeShipping(true);
        this.setTotalPrice();
    }

    public void makeShippingPaidFor() {
        this.setSumOfShippingPrices(this.getSumOfShippingPrices() + shippingPrice);
        this.setFreeShipping(false);
        this.setTotalPrice();
    }

}
