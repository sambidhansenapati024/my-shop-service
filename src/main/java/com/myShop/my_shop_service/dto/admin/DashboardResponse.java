package com.myShop.my_shop_service.dto.admin;

import com.myShop.my_shop_service.service.admin.DailySalesResponse;

import java.util.List;

public class DashboardResponse {

    private DashboardSummaryResponse summary;

    private List<MonthlySalesResponse> monthlySales;

    private List<TopSellingItemResponse> topSellingItems;

    private PaymentAnalysisResponse paymentAnalysis;

    private OrderAnalysisResponse orderAnalysis;

    private List<RecentBillResponse> recentBills;

    private List<DailySalesResponse> dailySales;

    public DashboardResponse() {
    }

    public DashboardResponse(
            DashboardSummaryResponse summary,
            List<MonthlySalesResponse> monthlySales,
            List<DailySalesResponse> dailySales,
            List<TopSellingItemResponse> topSellingItems,
            PaymentAnalysisResponse paymentAnalysis,
            OrderAnalysisResponse orderAnalysis,
            List<RecentBillResponse> recentBills
    ) {
        this.summary = summary;
        this.monthlySales = monthlySales;
        this.dailySales = dailySales;
        this.topSellingItems = topSellingItems;
        this.paymentAnalysis = paymentAnalysis;
        this.orderAnalysis = orderAnalysis;
        this.recentBills = recentBills;
    }

    public List<DailySalesResponse> getDailySales() {
        return dailySales;
    }

    public void setDailySales(List<DailySalesResponse> dailySales) {
        this.dailySales = dailySales;
    }

    public DashboardSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(DashboardSummaryResponse summary) {
        this.summary = summary;
    }

    public List<MonthlySalesResponse> getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(List<MonthlySalesResponse> monthlySales) {
        this.monthlySales = monthlySales;
    }

    public List<TopSellingItemResponse> getTopSellingItems() {
        return topSellingItems;
    }

    public void setTopSellingItems(List<TopSellingItemResponse> topSellingItems) {
        this.topSellingItems = topSellingItems;
    }

    public PaymentAnalysisResponse getPaymentAnalysis() {
        return paymentAnalysis;
    }

    public void setPaymentAnalysis(PaymentAnalysisResponse paymentAnalysis) {
        this.paymentAnalysis = paymentAnalysis;
    }

    public OrderAnalysisResponse getOrderAnalysis() {
        return orderAnalysis;
    }

    public void setOrderAnalysis(OrderAnalysisResponse orderAnalysis) {
        this.orderAnalysis = orderAnalysis;
    }

    public List<RecentBillResponse> getRecentBills() {
        return recentBills;
    }

    public void setRecentBills(List<RecentBillResponse> recentBills) {
        this.recentBills = recentBills;
    }
}