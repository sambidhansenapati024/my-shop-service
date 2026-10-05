package com.myShop.my_shop_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ManualBillPaymentRequest {

    @NotNull(message = "Paid amount is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Paid amount cannot be negative"
    )
    private BigDecimal paidAmount;

    @NotBlank(message = "Payment method is required")
    @Size(max = 30, message = "Payment method cannot exceed 30 characters")
    private String paymentMethod;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ManualBillPaymentRequest() {
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
}