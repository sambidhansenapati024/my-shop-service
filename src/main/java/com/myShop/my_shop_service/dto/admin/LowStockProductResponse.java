package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class LowStockProductResponse {

    private Long productId;

    private String productName;

    private String barcode;

    private String unit;

    private BigDecimal availableQuantity;


    public LowStockProductResponse() {
    }


    public LowStockProductResponse(
            Long productId,
            String productName,
            String barcode,
            String unit,
            BigDecimal availableQuantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.barcode = barcode;
        this.unit = unit;
        this.availableQuantity = availableQuantity;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(
            BigDecimal availableQuantity
    ) {
        this.availableQuantity = availableQuantity;
    }
}