package com.myShop.my_shop_service.dto.admin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductBatchRequest {

    @NotBlank(message = "Batch number is required")
    @Size(max = 100, message = "Batch number cannot exceed 100 characters")
    private String batchNumber;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Purchase price cannot be negative"
    )
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Selling price cannot be negative"
    )
    private BigDecimal sellingPrice;

    @NotNull(message = "Quantity is required")
    @DecimalMin(
            value = "0.001",
            message = "Quantity must be greater than zero"
    )
    private BigDecimal quantity;

    private LocalDate expiryDate;

    public ProductBatchRequest() {
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}