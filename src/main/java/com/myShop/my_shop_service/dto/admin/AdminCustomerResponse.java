package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;

public class AdminCustomerResponse {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;
    private Long totalOrders;
    private BigDecimal outstandingAmount;

    public AdminCustomerResponse() {
    }

    public AdminCustomerResponse(
            Long id,
            String name,
            String mobileNumber,
            String email,
            Long totalOrders,
            BigDecimal outstandingAmount
    ) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.totalOrders = totalOrders;
        this.outstandingAmount = outstandingAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }
}