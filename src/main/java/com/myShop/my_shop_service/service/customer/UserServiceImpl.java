package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.UserProfileResponse;
import com.myShop.my_shop_service.entity.User;

import com.myShop.my_shop_service.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<UserProfileResponse> getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || authentication.getPrincipal() == null) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        UserProfileResponse userProfile =
                new UserProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getMobileNumber(),
                        user.getEmail(),
                        user.getRole().name()
                );

        return ApiResponse.success(
                200,
                "User details fetched successfully",
                userProfile
        );
    }
}
