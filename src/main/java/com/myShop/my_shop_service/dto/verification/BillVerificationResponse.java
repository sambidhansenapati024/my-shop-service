package com.myShop.my_shop_service.dto.verification;

import com.myShop.my_shop_service.enums.PaymentStatus;

import java.math.BigDecimal;

public class BillVerificationResponse {

    private boolean valid;
    private String storeName;
    private String orderNumber;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;

    public BillVerificationResponse() {
    }

    public BillVerificationResponse(
            boolean valid,
            String storeName,
            String orderNumber,
            BigDecimal totalAmount,
            PaymentStatus paymentStatus
    ) {
        this.valid = valid;
        this.storeName = storeName;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}