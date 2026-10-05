package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductBatchResponse {

    private Long id;
    private Long productId;
    private String batchNumber;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal quantity;
    private LocalDate expiryDate;

    public ProductBatchResponse() {
    }

    public ProductBatchResponse(
            Long id,
            Long productId,
            String batchNumber,
            BigDecimal purchasePrice,
            BigDecimal sellingPrice,
            BigDecimal quantity,
            LocalDate expiryDate
    ) {
        this.id = id;
        this.productId = productId;
        this.batchNumber = batchNumber;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}