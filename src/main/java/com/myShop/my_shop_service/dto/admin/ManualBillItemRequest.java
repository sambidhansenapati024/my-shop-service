package com.myShop.my_shop_service.dto.admin;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ManualBillItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Product batch ID is required")
    private Long productBatchId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(
            value = "0.001",
            message = "Quantity must be greater than zero"
    )
    private BigDecimal quantity;

    public ManualBillItemRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductBatchId() {
        return productBatchId;
    }

    public void setProductBatchId(Long productBatchId) {
        this.productBatchId = productBatchId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}