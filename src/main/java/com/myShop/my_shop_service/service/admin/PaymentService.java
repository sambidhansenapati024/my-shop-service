package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.PaymentResponse;
import com.myShop.my_shop_service.entity.Payment;

import java.util.List;

public interface PaymentService {

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

}