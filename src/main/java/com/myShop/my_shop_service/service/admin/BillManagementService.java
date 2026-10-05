package com.myShop.my_shop_service.service.admin;


import com.myShop.my_shop_service.dto.auth.ApiResponse;

public interface BillManagementService {

    ApiResponse<?> getAllBills();
}
