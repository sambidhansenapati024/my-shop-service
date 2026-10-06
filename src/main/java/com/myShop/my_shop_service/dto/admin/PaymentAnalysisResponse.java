package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class PaymentAnalysisResponse {

    private Long paidBillCount;

    private Long partialBillCount;

    private Long unpaidBillCount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    public PaymentAnalysisResponse() {
    }

    public PaymentAnalysisResponse(
            Long paidBillCount,
            Long partialBillCount,
            Long unpaidBillCount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount
    ) {
        this.paidBillCount = paidBillCount;
        this.partialBillCount = partialBillCount;
        this.unpaidBillCount = unpaidBillCount;
        this.paidAmount = paidAmount;
        this.remainingAmount = remainingAmount;
    }

    public Long getPaidBillCount() {
        return paidBillCount;
    }

    public void setPaidBillCount(Long paidBillCount) {
        this.paidBillCount = paidBillCount;
    }

    public Long getPartialBillCount() {
        return partialBillCount;
    }

    public void setPartialBillCount(Long partialBillCount) {
        this.partialBillCount = partialBillCount;
    }

    public Long getUnpaidBillCount() {
        return unpaidBillCount;
    }

    public void setUnpaidBillCount(Long unpaidBillCount) {
        this.unpaidBillCount = unpaidBillCount;
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
}