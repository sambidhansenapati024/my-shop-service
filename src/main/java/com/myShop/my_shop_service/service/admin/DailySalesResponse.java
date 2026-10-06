package com.myShop.my_shop_service.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailySalesResponse {

    private LocalDate date;

    private BigDecimal salesAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private Long billCount;


    public DailySalesResponse() {
    }


    public DailySalesResponse(
            LocalDate date,
            BigDecimal salesAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            Long billCount
    ) {
        this.date = date;
        this.salesAmount = salesAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.billCount = billCount;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
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


    public Long getBillCount() {
        return billCount;
    }

    public void setBillCount(Long billCount) {
        this.billCount = billCount;
    }
}