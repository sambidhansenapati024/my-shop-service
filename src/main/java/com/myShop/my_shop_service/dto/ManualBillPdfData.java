package com.myShop.my_shop_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ManualBillPdfData {

    private String billNumber;
    private String customerName;
    private String customerMobile;
    private BigDecimal subtotal;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private List<ManualBillPdfItem> items;

    public ManualBillPdfData() {
    }

    public ManualBillPdfData(
            String billNumber,
            String customerName,
            String customerMobile,
            BigDecimal subtotal,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            String paymentStatus,
            LocalDateTime createdAt,
            List<ManualBillPdfItem> items
    ) {
        this.billNumber = billNumber;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.items = items;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ManualBillPdfItem> getItems() {
        return items;
    }

    public void setItems(List<ManualBillPdfItem> items) {
        this.items = items;
    }
}