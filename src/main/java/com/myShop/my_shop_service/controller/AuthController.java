package com.myShop.my_shop_service.controller;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.auth.LoginData;
import com.myShop.my_shop_service.dto.auth.LoginRequest;
import com.myShop.my_shop_service.dto.auth.RegisterRequest;
import com.myShop.my_shop_service.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        ApiResponse<Void> response =
                authService.register(request);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginData>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        ApiResponse<LoginData> response = authService.login(request);

        return ResponseEntity
                .status(response.getCode())
                .body(response);
    }
}
