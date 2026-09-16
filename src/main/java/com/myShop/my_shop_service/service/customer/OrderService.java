package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.CreateOrderRequest;
import org.springframework.web.multipart.MultipartFile;

public interface OrderService {

    ApiResponse<?> createOrder(
            CreateOrderRequest request, MultipartFile photo
    );

    ApiResponse<?> getMyOrders();

    ApiResponse<?> getOrderById(Long orderId);

    ApiResponse<?> getAllOrders();
}