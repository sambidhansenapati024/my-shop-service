package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.UserProfileResponse;

public interface UserService {

    ApiResponse<UserProfileResponse> getCurrentUser();
}
