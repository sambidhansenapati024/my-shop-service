package com.myShop.my_shop_service.dto.customer;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private String itemName;
    private BigDecimal quantity;
    private String unit;

    // BILLING FIELDS
    private BigDecimal unitPrice;
    private BigDecimal itemTotal;

    public OrderItemResponse() {
    }

    public OrderItemResponse(
            Long id,
            String itemName,
            BigDecimal quantity,
            String unit
    ) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }
}