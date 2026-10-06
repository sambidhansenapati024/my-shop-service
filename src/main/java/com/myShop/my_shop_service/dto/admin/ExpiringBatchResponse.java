package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpiringBatchResponse {

    private Long batchId;

    private Long productId;

    private String productName;

    private String batchNumber;

    private BigDecimal quantity;

    private String unit;

    private LocalDate expiryDate;

    private Long daysRemaining;


    public ExpiringBatchResponse() {
    }


    public ExpiringBatchResponse(
            Long batchId,
            Long productId,
            String productName,
            String batchNumber,
            BigDecimal quantity,
            String unit,
            LocalDate expiryDate,
            Long daysRemaining
    ) {
        this.batchId = batchId;
        this.productId = productId;
        this.productName = productName;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.daysRemaining = daysRemaining;
    }


    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
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


    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
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


    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }


    public Long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(Long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
}