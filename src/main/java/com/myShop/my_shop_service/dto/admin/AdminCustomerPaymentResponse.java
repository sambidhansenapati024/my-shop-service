package com.myShop.my_shop_service.dto.admin;

import com.myShop.my_shop_service.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminCustomerPaymentResponse {

    private Long id;
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;

    public AdminCustomerPaymentResponse() {
    }

    public AdminCustomerPaymentResponse(
            Long id,
            String orderNumber,
            BigDecimal amount,
            PaymentMethod paymentMethod,
            LocalDateTime paymentDate
    ) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}