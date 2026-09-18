package com.myShop.my_shop_service.controller.admin;


import com.myShop.my_shop_service.dto.PaymentRequest;
import com.myShop.my_shop_service.dto.admin.CalculateBillRequest;
import com.myShop.my_shop_service.dto.admin.UpdateOrderStatusRequest;
import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.service.admin.AdminOrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(
            AdminOrderService adminOrderService
    ) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllOrders() {

        ApiResponse<?> response =
                adminOrderService.getAllOrders();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrderById(
            @PathVariable Long orderId
    ) {

        ApiResponse<?> response =
                adminOrderService.getOrderById(orderId);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        ApiResponse<?> response =
                adminOrderService.updateOrderStatus(
                        orderId,
                        request
                );

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @PostMapping("/{orderId}/calculate")
    public ResponseEntity<ApiResponse<?>> calculateBill(
            @PathVariable Long orderId,
            @Valid @RequestBody CalculateBillRequest request
    ) {

        ApiResponse<?> response =
                adminOrderService.calculateBill(
                        orderId,
                        request
                );

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @PostMapping("/{orderId}/generate-bill")
    public ResponseEntity<ApiResponse<?>> generateBill(
            @PathVariable Long orderId,
            @Valid @RequestBody CalculateBillRequest request
    ) {
        ApiResponse<?> response =
                adminOrderService.generateBill(orderId, request);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<ApiResponse<?>> makePayment(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequest request
    ) {

        ApiResponse<?> response =
                adminOrderService.makePayment(
                        orderId,
                        request
                );

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @GetMapping("/bills")
    public ResponseEntity<ApiResponse<?>> getAllBills() {

        ApiResponse<?> response =
                adminOrderService.getAllBills();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }
}