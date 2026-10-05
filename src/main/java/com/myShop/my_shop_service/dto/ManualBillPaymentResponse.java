package com.myShop.my_shop_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ManualBillPaymentResponse {

    private Long id;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime paidAt;

    public ManualBillPaymentResponse() {
    }

    public ManualBillPaymentResponse(
            Long id,
            BigDecimal amount,
            String paymentMethod,
            LocalDateTime paidAt
    ) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}