package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.UserProfileResponse;
import com.myShop.my_shop_service.service.customer.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getById")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser() {

        ApiResponse<UserProfileResponse> response =
                userService.getCurrentUser();

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }
}