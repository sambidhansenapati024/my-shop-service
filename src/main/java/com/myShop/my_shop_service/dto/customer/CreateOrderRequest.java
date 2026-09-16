package com.myShop.my_shop_service.dto.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @NotBlank(message = "Order type is required")
    private String orderType;

    private List<@Valid OrderItemRequest> items;

    private String photoNote;

    public CreateOrderRequest() {
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getPhotoNote() {
        return photoNote;
    }

    public void setPhotoNote(String photoNote) {
        this.photoNote = photoNote;
    }
}
