package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class AdminCustomerDetailsResponse {

    private Long id;
    private String name;
    private String mobileNumber;
    private String email;

    private Long totalOrders;
    private BigDecimal outstandingAmount;

    private List<AdminCustomerOrderResponse> orders;
    private List<AdminCustomerPaymentResponse> payments;

    public AdminCustomerDetailsResponse() {
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

    public void setOutstandingAmount(
            BigDecimal outstandingAmount
    ) {
        this.outstandingAmount = outstandingAmount;
    }

    public List<AdminCustomerOrderResponse> getOrders() {
        return orders;
    }

    public void setOrders(
            List<AdminCustomerOrderResponse> orders
    ) {
        this.orders = orders;
    }

    public List<AdminCustomerPaymentResponse> getPayments() {
        return payments;
    }

    public void setPayments(
            List<AdminCustomerPaymentResponse> payments
    ) {
        this.payments = payments;
    }
}