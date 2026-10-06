package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class MonthlySalesResponse {

    private String month;

    private Integer year;

    private BigDecimal salesAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private Long billCount;

    public MonthlySalesResponse() {
    }

    public MonthlySalesResponse(
            String month,
            Integer year,
            BigDecimal salesAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            Long billCount
    ) {
        this.month = month;
        this.year = year;
        this.salesAmount = salesAmount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
        this.billCount = billCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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