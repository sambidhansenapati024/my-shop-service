package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.customer.OrderPdfData;

public interface OrderPdfService {

    byte[] generateOrderPdf(OrderPdfData order);

    byte[] generateBillPdf(OrderPdfData order);
}