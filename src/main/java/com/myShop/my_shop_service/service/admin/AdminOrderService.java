package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.PaymentRequest;
import com.myShop.my_shop_service.dto.admin.CalculateBillRequest;
import com.myShop.my_shop_service.dto.admin.UpdateOrderStatusRequest;
import com.myShop.my_shop_service.dto.auth.ApiResponse;

public interface AdminOrderService {

    ApiResponse<?> getAllOrders();

    ApiResponse<?> getOrderById(Long orderId);

    ApiResponse<?> updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    ApiResponse<?> calculateBill(
            Long orderId,
            CalculateBillRequest request
    );

    ApiResponse<?> generateBill(
            Long orderId,
            CalculateBillRequest request
    );
    ApiResponse<?> makePayment(Long orderId, PaymentRequest paymentRequest);
}