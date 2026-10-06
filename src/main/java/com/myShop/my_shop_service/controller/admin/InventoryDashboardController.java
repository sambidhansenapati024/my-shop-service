package com.myShop.my_shop_service.controller.admin;

import com.myShop.my_shop_service.dto.admin.InventoryDashboardResponse;
import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.service.admin.InventoryDashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard/inventory")
public class InventoryDashboardController {

    private final InventoryDashboardService inventoryDashboardService;


    public InventoryDashboardController(
            InventoryDashboardService inventoryDashboardService
    ) {
        this.inventoryDashboardService = inventoryDashboardService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<?>> getInventoryDashboard() {

        InventoryDashboardResponse dashboard =
                inventoryDashboardService.getInventoryDashboard();

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Inventory dashboard data fetched successfully",
                        dashboard
                )
        );
    }
}
