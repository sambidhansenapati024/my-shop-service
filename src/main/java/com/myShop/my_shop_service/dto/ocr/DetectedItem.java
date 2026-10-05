package com.myShop.my_shop_service.dto.ocr;

import java.math.BigDecimal;

public class DetectedItem {

    private String itemName;

    private BigDecimal quantity;

    private String unit;

    public DetectedItem() {
    }

    public DetectedItem(
            String itemName,
            BigDecimal quantity,
            String unit
    ) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}