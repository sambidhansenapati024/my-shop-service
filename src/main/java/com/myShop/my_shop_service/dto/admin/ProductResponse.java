package com.myShop.my_shop_service.dto.admin;

public class ProductResponse {

    private Long id;
    private String productName;
    private String barcode;
    private String unit;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String productName,
            String barcode,
            String unit
    ) {
        this.id = id;
        this.productName = productName;
        this.barcode = barcode;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getUnit() {
        return unit;
    }
}