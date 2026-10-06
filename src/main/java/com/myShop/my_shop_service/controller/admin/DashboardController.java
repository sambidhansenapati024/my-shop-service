package com.myShop.my_shop_service.controller.admin;

import com.myShop.my_shop_service.dto.admin.DashboardResponse;
import com.myShop.my_shop_service.dto.auth.ApiResponse;

import com.myShop.my_shop_service.service.admin.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getDashboard() {

        DashboardResponse dashboard =
                dashboardService.getDashboard();

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Dashboard data fetched successfully",
                        dashboard
                )
        );
    }
}