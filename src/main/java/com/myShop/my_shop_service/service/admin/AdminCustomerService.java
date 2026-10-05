package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.admin.AdminCustomerDetailsResponse;
import com.myShop.my_shop_service.dto.admin.AdminCustomerResponse;

import java.util.List;

public interface AdminCustomerService {

    List<AdminCustomerResponse> getAllCustomers();

    AdminCustomerDetailsResponse getCustomerDetails(
            Long customerId
    );
}