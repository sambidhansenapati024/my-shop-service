package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.admin.AdminCustomerDetailsResponse;
import com.myShop.my_shop_service.dto.admin.AdminCustomerResponse;
import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.service.admin.AdminCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    public AdminCustomerController(
            AdminCustomerService adminCustomerService
    ) {
        this.adminCustomerService =
                adminCustomerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminCustomerResponse>>>
    getAllCustomers() {

        List<AdminCustomerResponse> customers =
                adminCustomerService.getAllCustomers();

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Customers fetched successfully",
                        customers
                )
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<AdminCustomerDetailsResponse>>
    getCustomerDetails(
            @PathVariable Long customerId
    ) {

        AdminCustomerDetailsResponse customer =
                adminCustomerService.getCustomerDetails(
                        customerId
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Customer details fetched successfully",
                        customer
                )
        );
    }
}