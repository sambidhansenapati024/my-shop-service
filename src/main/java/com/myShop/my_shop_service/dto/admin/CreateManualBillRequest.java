package com.myShop.my_shop_service.dto.admin;

import jakarta.validation.constraints.Size;

public class CreateManualBillRequest {

    @Size(
            max = 150,
            message = "Customer name cannot exceed 150 characters"
    )
    private String customerName;

    @Size(
            max = 20,
            message = "Customer mobile cannot exceed 20 characters"
    )
    private String customerMobile;

    public CreateManualBillRequest() {
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
}