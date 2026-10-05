package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class ManualBillItemResponse {

    private Long id;

    private Long productId;

    private Long productBatchId;

    private String itemName;

    private BigDecimal quantity;

    private String unit;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    public ManualBillItemResponse() {
    }

    public ManualBillItemResponse(
            Long id,
            Long productId,
            Long productBatchId,
            String itemName,
            BigDecimal quantity,
            String unit,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ) {
        this.id = id;
        this.productId = productId;
        this.productBatchId = productBatchId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getProductBatchId() {
        return productBatchId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}