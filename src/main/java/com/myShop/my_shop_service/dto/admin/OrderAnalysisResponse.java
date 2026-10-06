package com.myShop.my_shop_service.dto.admin;

import java.util.Map;

public class OrderAnalysisResponse {

    private Long customerOrders;

    private Long manualBills;

    private Long totalBills;

    private Map<String, Long> statusCounts;

    public OrderAnalysisResponse() {
    }

    public OrderAnalysisResponse(
            Long customerOrders,
            Long manualBills,
            Long totalBills,
            Map<String, Long> statusCounts
    ) {
        this.customerOrders = customerOrders;
        this.manualBills = manualBills;
        this.totalBills = totalBills;
        this.statusCounts = statusCounts;
    }

    public Long getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(Long customerOrders) {
        this.customerOrders = customerOrders;
    }

    public Long getManualBills() {
        return manualBills;
    }

    public void setManualBills(Long manualBills) {
        this.manualBills = manualBills;
    }

    public Long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(Long totalBills) {
        this.totalBills = totalBills;
    }

    public Map<String, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<String, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }
}