package com.myShop.my_shop_service.dto.customer;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private String itemName;
    private BigDecimal quantity;
    private String unit;

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

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
