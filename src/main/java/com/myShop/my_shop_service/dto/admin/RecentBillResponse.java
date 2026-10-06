package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecentBillResponse {

    private Long id;

    private String billNumber;

    private String billType;

    private String customerName;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private String paymentStatus;

    private LocalDateTime billedAt;

    public RecentBillResponse() {
    }

    public RecentBillResponse(
            Long id,
            String billNumber,
            String billType,
            String customerName,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            String paymentStatus,
            LocalDateTime billedAt
    ) {
        this.id = id;
        this.billNumber = billNumber;
        this.billType = billType;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.paymentStatus = paymentStatus;
        this.billedAt = billedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getBilledAt() {
        return billedAt;
    }

    public void setBilledAt(LocalDateTime billedAt) {
        this.billedAt = billedAt;
    }
}