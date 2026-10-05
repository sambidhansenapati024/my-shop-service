package com.myShop.my_shop_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_bill_items")
public class ManualBillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bill_id",
            nullable = false
    )
    private ManualBill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_batch_id"
    )
    private ProductBatch productBatch;

    @Column(
            name = "item_name",
            nullable = false,
            length = 200
    )
    private String itemName;

    @Column(
            name = "quantity",
            nullable = false,
            precision = 12,
            scale = 3
    )
    private BigDecimal quantity;

    @Column(
            name = "unit",
            nullable = false,
            length = 30
    )
    private String unit;

    @Column(
            name = "unit_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal unitPrice;

    @Column(
            name = "total_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalPrice;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    public ManualBillItem() {
    }

    public Long getId() {
        return id;
    }

    public ManualBill getBill() {
        return bill;
    }

    public void setBill(ManualBill bill) {
        this.bill = bill;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductBatch getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(ProductBatch productBatch) {
        this.productBatch = productBatch;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}