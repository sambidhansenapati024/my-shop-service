package com.myShop.my_shop_service.service.manualBill;

import com.myShop.my_shop_service.dto.ManualBillPdfData;

public interface ManualBillPdfService {

    byte[] generateBillPdf(Long billId);

}