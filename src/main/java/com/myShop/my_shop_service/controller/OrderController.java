package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.CreateOrderRequest;
import com.myShop.my_shop_service.service.customer.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> createOrder(
            @Valid @RequestPart("order") CreateOrderRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {

        ApiResponse<?> response =
                orderService.createOrder(request, photo);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @GetMapping("/getMyOrders")
    public ResponseEntity<ApiResponse<?>> getMyOrders() {

        ApiResponse<?> response = orderService.getMyOrders();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(
            @PathVariable Long orderId
    ) {

        ApiResponse<?> response =
                orderService.getOrderById(orderId);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllOrders() {

        ApiResponse<?> response = orderService.getAllOrders();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }
}