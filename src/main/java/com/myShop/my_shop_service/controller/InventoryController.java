package com.myShop.my_shop_service.controller;
import com.myShop.my_shop_service.dto.admin.ProductBatchRequest;
import com.myShop.my_shop_service.dto.admin.ProductRequest;
import com.myShop.my_shop_service.service.inventory.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {

        return ResponseEntity.ok(
                inventoryService.createProduct(request)
        );
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {

        return ResponseEntity.ok(
                inventoryService.getAllProducts()
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProduct(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                inventoryService.getProduct(productId)
        );
    }

    @GetMapping("/products/barcode/{barcode}")
    public ResponseEntity<?> findProductByBarcode(
            @PathVariable String barcode
    ) {

        return ResponseEntity.ok(
                inventoryService.findProductByBarcode(barcode)
        );
    }

    @PostMapping("/products/{productId}/batches")
    public ResponseEntity<?> addBatch(
            @PathVariable Long productId,
            @Valid @RequestBody ProductBatchRequest request
    ) {

        return ResponseEntity.ok(
                inventoryService.addBatch(
                        productId,
                        request
                )
        );
    }

    @GetMapping("/products/{productId}/batches")
    public ResponseEntity<?> getProductBatches(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                inventoryService.getProductBatches(productId)
        );
    }
}