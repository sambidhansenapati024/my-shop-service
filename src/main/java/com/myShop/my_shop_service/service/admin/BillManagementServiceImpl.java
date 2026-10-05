package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class BillManagementServiceImpl implements BillManagementService {

    @Override
    public ApiResponse<?> getAllBills() {

        return new ApiResponse<>(
                "true",
                200,
                "Bills fetched successfully",
                null
        );
    }
}