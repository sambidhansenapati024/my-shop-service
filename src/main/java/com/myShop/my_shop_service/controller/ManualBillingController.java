package com.myShop.my_shop_service.controller;
import com.myShop.my_shop_service.dto.ManualBillPaymentRequest;
import com.myShop.my_shop_service.dto.ManualBillPaymentResponse;
import com.myShop.my_shop_service.dto.admin.CreateManualBillRequest;
import com.myShop.my_shop_service.dto.admin.ManualBillItemRequest;
import com.myShop.my_shop_service.dto.admin.ManualBillResponse;
import com.myShop.my_shop_service.service.manualBill.ManualBillPdfService;
import com.myShop.my_shop_service.service.manualBill.ManualBillingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/billing")
public class ManualBillingController {

    private final ManualBillingService manualBillingService;
    private final ManualBillPdfService manualBillPdfService;

    public ManualBillingController(
            ManualBillingService manualBillingService,
            ManualBillPdfService manualBillPdfService
    ) {
        this.manualBillingService = manualBillingService;
        this.manualBillPdfService = manualBillPdfService;
    }

    @GetMapping("/products/barcode/{barcode}")
    public ResponseEntity<?> findProductByBarcode(
            @PathVariable String barcode
    ) {

        return ResponseEntity.ok(
                manualBillingService.findProductByBarcode(
                        barcode
                )
        );
    }

    @GetMapping("/products/{productId}/batches")
    public ResponseEntity<?> getAvailableBatches(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                manualBillingService.getAvailableBatches(
                        productId
                )
        );
    }

    @PostMapping("/bills")
    public ResponseEntity<?> createBill(
            @Valid @RequestBody CreateManualBillRequest request
    ) {

        return ResponseEntity.ok(
                manualBillingService.createBill(request)
        );
    }

    @GetMapping("/bills/{billId}")
    public ResponseEntity<?> getBill(
            @PathVariable Long billId
    ) {

        return ResponseEntity.ok(
                manualBillingService.getBill(billId)
        );
    }

    @PostMapping("/bills/{billId}/items")
    public ResponseEntity<?> addItem(
            @PathVariable Long billId,
            @Valid @RequestBody ManualBillItemRequest request
    ) {

        return ResponseEntity.ok(
                manualBillingService.addItem(
                        billId,
                        request
                )
        );
    }

    @DeleteMapping("/bills/{billId}/items/{itemId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Long billId,
            @PathVariable Long itemId
    ) {

        return ResponseEntity.ok(
                manualBillingService.removeItem(
                        billId,
                        itemId
                )
        );
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(
                manualBillingService.getAllProducts()
        );
    }

    @PostMapping("/bills/{billId}/payment")
    public ResponseEntity<?> processPayment(
            @PathVariable Long billId,
            @Valid @RequestBody ManualBillPaymentRequest request
    ) {
        return ResponseEntity.ok(
                manualBillingService.processPayment(
                        billId,
                        request
                )
        );
    }

    @GetMapping(
            value = "/bills/{billId}/pdf",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadBillPdf(
            @PathVariable Long billId
    ) {

        byte[] pdf =
                manualBillPdfService.generateBillPdf(
                        billId
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"manual-bill-"
                                + billId
                                + ".pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/bills/{billId}/generate")
    public ResponseEntity<ManualBillResponse> generateBill(
            @PathVariable Long billId
    ) {
        ManualBillResponse response =
                manualBillingService.generateBill(billId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bills/{billId}/payments")
    public ResponseEntity<List<ManualBillPaymentResponse>> getPaymentHistory(
            @PathVariable Long billId
    ) {
        List<ManualBillPaymentResponse> payments =
                manualBillingService.getPaymentHistory(billId);

        return ResponseEntity.ok(payments);
    }
}