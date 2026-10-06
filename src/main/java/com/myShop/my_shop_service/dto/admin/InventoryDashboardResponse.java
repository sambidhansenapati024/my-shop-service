package com.myShop.my_shop_service.dto.admin;

import java.util.List;

public class InventoryDashboardResponse {

    private InventorySummaryResponse summary;

    private InventoryValueResponse value;

    private List<LowStockProductResponse> lowStockProducts;

    private List<ExpiringBatchResponse> expiringBatches;

    private List<ExpiringBatchResponse> expiredBatches;



    public InventoryDashboardResponse() {
    }


    public InventoryDashboardResponse(
            InventorySummaryResponse summary,
            InventoryValueResponse value,
            List<LowStockProductResponse> lowStockProducts,
            List<ExpiringBatchResponse> expiringBatches,
            List<ExpiringBatchResponse> expiredBatches
    ) {
        this.summary = summary;
        this.value = value;
        this.lowStockProducts = lowStockProducts;
        this.expiringBatches = expiringBatches;
        this.expiredBatches = expiredBatches;
    }

    public InventoryValueResponse getValue() {
        return value;
    }

    public void setValue(InventoryValueResponse value) {
        this.value = value;
    }


    public InventorySummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(InventorySummaryResponse summary) {
        this.summary = summary;
    }


    public List<LowStockProductResponse> getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(
            List<LowStockProductResponse> lowStockProducts
    ) {
        this.lowStockProducts = lowStockProducts;
    }


    public List<ExpiringBatchResponse> getExpiringBatches() {
        return expiringBatches;
    }

    public void setExpiringBatches(
            List<ExpiringBatchResponse> expiringBatches
    ) {
        this.expiringBatches = expiringBatches;
    }


    public List<ExpiringBatchResponse> getExpiredBatches() {
        return expiredBatches;
    }

    public void setExpiredBatches(
            List<ExpiringBatchResponse> expiredBatches
    ) {
        this.expiredBatches = expiredBatches;
    }
}