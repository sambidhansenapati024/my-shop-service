package com.myShop.my_shop_service.dto.admin;

import com.myShop.my_shop_service.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ManualBillResponse {

    private Long id;

    private String billNumber;

    private String customerName;

    private String customerMobile;

    private BigDecimal subtotal;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ManualBillItemResponse> items;

    private LocalDateTime billedAt;

    public LocalDateTime getBilledAt() {
        return billedAt;
    }

    public void setBilledAt(LocalDateTime billedAt) {
        this.billedAt = billedAt;
    }

    public ManualBillResponse() {
    }

    public ManualBillResponse(
            Long id,
            String billNumber,
            String customerName,
            String customerMobile,
            BigDecimal subtotal,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            PaymentStatus paymentStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<ManualBillItemResponse> items
    ) {
        this.id = id;
        this.billNumber = billNumber;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ManualBillItemResponse> getItems() {
        return items;
    }
}