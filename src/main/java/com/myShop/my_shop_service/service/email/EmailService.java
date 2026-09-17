package com.myShop.my_shop_service.service.email;

import java.time.LocalDateTime;

public interface EmailService {

    void sendOrderEmail(
            String to,
            String customerName,
            String orderNumber,
            String orderType,
            String status,
            LocalDateTime createdAt,
            byte[] pdfBytes
    );

    void sendBillGeneratedEmail(
            String to,
            String customerName,
            String orderNumber,
            byte[] pdfBytes
    );

    void sendBillModifiedEmail(
            String to,
            String customerName,
            String orderNumber,
            byte[] pdfBytes
    );
}