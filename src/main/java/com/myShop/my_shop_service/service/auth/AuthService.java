package com.myShop.my_shop_service.service.auth;


import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.auth.LoginData;
import com.myShop.my_shop_service.dto.auth.LoginRequest;
import com.myShop.my_shop_service.dto.auth.RegisterRequest;

public interface AuthService {

    ApiResponse<Void> register(RegisterRequest request);
    ApiResponse<LoginData> login(LoginRequest request);
}