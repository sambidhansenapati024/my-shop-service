package com.myShop.my_shop_service.service.auth;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.auth.LoginData;
import com.myShop.my_shop_service.dto.auth.LoginRequest;
import com.myShop.my_shop_service.dto.auth.RegisterRequest;
import com.myShop.my_shop_service.entity.User;
import com.myShop.my_shop_service.enums.Role;
import com.myShop.my_shop_service.repo.UserRepository;
import com.myShop.my_shop_service.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse<Void> register(RegisterRequest request) {

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            return ApiResponse.error(
                    409,
                    "Mobile number is already registered"
            );
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && userRepository.existsByEmail(request.getEmail())) {

            return ApiResponse.error(
                    409,
                    "Email is already registered"
            );
        }

        String passwordHash =
                passwordEncoder.encode(request.getPassword());

        User user = new User();

        user.setName(request.getName());
        user.setMobileNumber(request.getMobileNumber());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordHash);
        user.setRole(Role.USER);

        userRepository.save(user);

        return ApiResponse.success(
                200,
                "Registration successful"
        );
    }

    @Override
    public ApiResponse<LoginData> login(LoginRequest request) {

        String loginId = request.getLoginId().trim();

        User user;

        if (loginId.contains("@")) {
            user = userRepository
                    .findByEmail(loginId)
                    .orElse(null);
        } else {
            user = userRepository
                    .findByMobileNumber(loginId)
                    .orElse(null);
        }

        if (user == null) {
            return ApiResponse.error(
                    401,
                    "Invalid mobile number or password"
            );
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            return ApiResponse.error(
                    401,
                    "Invalid mobile number or password"
            );
        }

        String accessToken = jwtService.generateAccessToken(user);

        LoginData loginData = new LoginData(
                accessToken,
                null
        );

        return ApiResponse.success(
                200,
                "Login successful",
                loginData
        );
    }
}
