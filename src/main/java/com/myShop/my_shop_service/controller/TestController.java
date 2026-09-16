package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public ApiResponse<String> protectedEndpoint(
            Authentication authentication
    ) {

        String username = authentication.getName();

        return ApiResponse.success(
                200,
                "JWT authentication successful",
                username
        );
    }
}