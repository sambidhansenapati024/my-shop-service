package com.myShop.my_shop_service.dto.admin;

public class InventorySummaryResponse {

    private Long totalProducts;

    private Long totalBatches;

    private Long lowStockProducts;

    private Long outOfStockProducts;

    private Long expiringSoonBatches;

    private Long expiredBatches;


    public InventorySummaryResponse() {
    }


    public InventorySummaryResponse(
            Long totalProducts,
            Long totalBatches,
            Long lowStockProducts,
            Long outOfStockProducts,
            Long expiringSoonBatches,
            Long expiredBatches
    ) {
        this.totalProducts = totalProducts;
        this.totalBatches = totalBatches;
        this.lowStockProducts = lowStockProducts;
        this.outOfStockProducts = outOfStockProducts;
        this.expiringSoonBatches = expiringSoonBatches;
        this.expiredBatches = expiredBatches;
    }


    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }


    public Long getTotalBatches() {
        return totalBatches;
    }

    public void setTotalBatches(Long totalBatches) {
        this.totalBatches = totalBatches;
    }


    public Long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(Long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }


    public Long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(Long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }


    public Long getExpiringSoonBatches() {
        return expiringSoonBatches;
    }

    public void setExpiringSoonBatches(Long expiringSoonBatches) {
        this.expiringSoonBatches = expiringSoonBatches;
    }


    public Long getExpiredBatches() {
        return expiredBatches;
    }

    public void setExpiredBatches(Long expiredBatches) {
        this.expiredBatches = expiredBatches;
    }
}