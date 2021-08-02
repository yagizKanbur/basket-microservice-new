package com.ty.basketmicroservice.model;

import com.ty.basketmicroservice.enums.BasketItemStatus;
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

    public BasketInfo() {
        sumOfShippingPrices = 12.00;
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

        if (this.getSumOfProductPrices() > 60 && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void calculatePriceAfterDecreaseInQuantity(BasketItem item) {
        Double newPrice = this.getSumOfProductPrices() - item.getProductPrice();
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= 60 && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
    }

    public void calculatePriceAfterChangingQuantity(BasketItem item, int changeInQuantity) {
        Double newPrice = this.getSumOfProductPrices() + item.getProductPrice() * changeInQuantity;
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= 60 && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
        if (this.getSumOfProductPrices() > 60 && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void calculatePriceAfterRemovingItem(BasketItem item) {
        Double newPrice = this.getSumOfProductPrices() - item.getProductPrice() * item.getQuantity();
        this.setSumOfProductPrices(newPrice);
        this.setTotalPrice();
        if (this.getSumOfProductPrices() <= 60 && this.isFreeShipping()) {
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
        if (this.getSumOfProductPrices() <= 60 && this.isFreeShipping()) {
            makeShippingPaidFor();
        }
        if (this.getSumOfProductPrices() > 60 && !this.isFreeShipping()) {
            makeShippingFree();
        }
    }

    public void makeShippingFree() {
        this.setSumOfShippingPrices(this.getSumOfShippingPrices() - 12.00);
        this.setFreeShipping(true);
        this.setTotalPrice();
    }

    public void makeShippingPaidFor() {
        this.setSumOfShippingPrices(this.getSumOfShippingPrices() + 12.00);
        this.setFreeShipping(false);
        this.setTotalPrice();
    }

}
