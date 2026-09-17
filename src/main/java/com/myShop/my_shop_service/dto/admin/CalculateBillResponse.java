package com.myShop.my_shop_service.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class CalculateBillResponse {

    private List<ItemCalculationResponse> items;

    private BigDecimal totalAmount;

    public CalculateBillResponse() {
    }

    public CalculateBillResponse(
            List<ItemCalculationResponse> items,
            BigDecimal totalAmount
    ) {
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public List<ItemCalculationResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemCalculationResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static class ItemCalculationResponse {

        private Long itemId;

        private String itemName;

        private BigDecimal quantity;

        private String unit;

        private BigDecimal unitPrice;

        private BigDecimal itemTotal;

        public ItemCalculationResponse() {
        }

        public ItemCalculationResponse(
                Long itemId,
                String itemName,
                BigDecimal quantity,
                String unit,
                BigDecimal unitPrice,
                BigDecimal itemTotal
        ) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unit = unit;
            this.unitPrice = unitPrice;
            this.itemTotal = itemTotal;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getItemTotal() {
            return itemTotal;
        }

        public void setItemTotal(BigDecimal itemTotal) {
            this.itemTotal = itemTotal;
        }
    }
}