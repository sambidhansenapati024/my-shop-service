package com.myShop.my_shop_service.controller.verification;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.verification.BillVerificationResponse;
import com.myShop.my_shop_service.service.verification.BillVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verify")
public class BillVerificationController {

    private final BillVerificationService billVerificationService;

    public BillVerificationController(
            BillVerificationService billVerificationService
    ) {
        this.billVerificationService =
                billVerificationService;
    }

    @GetMapping("/bill/{verificationCode}")
    public ResponseEntity<ApiResponse<?>> verifyBill(
            @PathVariable String verificationCode
    ) {

        BillVerificationResponse response =
                billVerificationService.verifyBill(
                        verificationCode
                );

        if (!response.isValid()) {

            return ResponseEntity.ok(
                    ApiResponse.success(
                            200,
                            "Bill verification failed",
                            response
                    )
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "Bill verified successfully",
                        response
                )
        );
    }
}