package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private BigDecimal todaySales;

    private BigDecimal monthSales;

    private BigDecimal totalSales;

    private BigDecimal totalPaid;

    private BigDecimal totalRemaining;

    private Long billCount;

    private BigDecimal averageBillValue;

    public DashboardSummaryResponse() {
    }

    public BigDecimal getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(BigDecimal todaySales) {
        this.todaySales = todaySales;
    }

    public BigDecimal getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(BigDecimal monthSales) {
        this.monthSales = monthSales;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalRemaining() {
        return totalRemaining;
    }

    public void setTotalRemaining(BigDecimal totalRemaining) {
        this.totalRemaining = totalRemaining;
    }

    public Long getBillCount() {
        return billCount;
    }

    public void setBillCount(Long billCount) {
        this.billCount = billCount;
    }

    public BigDecimal getAverageBillValue() {
        return averageBillValue;
    }

    public void setAverageBillValue(BigDecimal averageBillValue) {
        this.averageBillValue = averageBillValue;
    }
}