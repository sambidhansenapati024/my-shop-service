package com.myShop.my_shop_service.dto.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class CalculateBillRequest {

    @NotEmpty(message = "Item prices are required")
    @Valid
    private List<ItemPriceRequest> items;

    public CalculateBillRequest() {
    }

    public List<ItemPriceRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemPriceRequest> items) {
        this.items = items;
    }

    public static class ItemPriceRequest {

        private Long itemId;

        private BigDecimal unitPrice;

        public ItemPriceRequest() {
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}