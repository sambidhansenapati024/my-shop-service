package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class TopSellingItemResponse {

    private String itemName;

    private String unit;

    private BigDecimal quantitySold;

    private BigDecimal salesAmount;

    private Long numberOfBills;

    public TopSellingItemResponse() {
    }

    public TopSellingItemResponse(
            String itemName,
            String unit,
            BigDecimal quantitySold,
            BigDecimal salesAmount,
            Long numberOfBills
    ) {
        this.itemName = itemName;
        this.unit = unit;
        this.quantitySold = quantitySold;
        this.salesAmount = salesAmount;
        this.numberOfBills = numberOfBills;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(BigDecimal quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public Long getNumberOfBills() {
        return numberOfBills;
    }

    public void setNumberOfBills(Long numberOfBills) {
        this.numberOfBills = numberOfBills;
    }
}