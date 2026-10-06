package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class InventoryValueResponse {

    private BigDecimal purchaseValue;

    private BigDecimal sellingValue;

    private BigDecimal potentialMargin;

    private BigDecimal marginPercentage;


    public InventoryValueResponse() {
    }


    public InventoryValueResponse(
            BigDecimal purchaseValue,
            BigDecimal sellingValue,
            BigDecimal potentialMargin,
            BigDecimal marginPercentage
    ) {
        this.purchaseValue = purchaseValue;
        this.sellingValue = sellingValue;
        this.potentialMargin = potentialMargin;
        this.marginPercentage = marginPercentage;
    }


    public BigDecimal getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(BigDecimal purchaseValue) {
        this.purchaseValue = purchaseValue;
    }


    public BigDecimal getSellingValue() {
        return sellingValue;
    }

    public void setSellingValue(BigDecimal sellingValue) {
        this.sellingValue = sellingValue;
    }


    public BigDecimal getPotentialMargin() {
        return potentialMargin;
    }

    public void setPotentialMargin(BigDecimal potentialMargin) {
        this.potentialMargin = potentialMargin;
    }


    public BigDecimal getMarginPercentage() {
        return marginPercentage;
    }

    public void setMarginPercentage(BigDecimal marginPercentage) {
        this.marginPercentage = marginPercentage;
    }
}