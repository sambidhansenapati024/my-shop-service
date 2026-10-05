package com.myShop.my_shop_service.service.manualBill;


import com.myShop.my_shop_service.dto.ManualBillPaymentRequest;
import com.myShop.my_shop_service.dto.ManualBillPaymentResponse;
import com.myShop.my_shop_service.dto.admin.*;

import java.util.List;

public interface ManualBillingService {

    ProductResponse findProductByBarcode(String barcode);

    List<ProductBatchResponse> getAvailableBatches(
            Long productId
    );

    ManualBillResponse createBill(
            CreateManualBillRequest request
    );

    ManualBillResponse getBill(
            Long billId
    );

    ManualBillResponse addItem(
            Long billId,
            ManualBillItemRequest request
    );

    ManualBillResponse removeItem(
            Long billId,
            Long itemId
    );

    List<ProductResponse> getAllProducts();

    ManualBillResponse processPayment(
            Long billId,
            ManualBillPaymentRequest request
    );

    ManualBillResponse generateBill(Long billId);

    List<ManualBillPaymentResponse> getPaymentHistory(Long billId);
}