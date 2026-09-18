
package com.myShop.my_shop_service.controller.admin;
import com.myShop.my_shop_service.dto.PaymentResponse;
import com.myShop.my_shop_service.service.admin.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentResponse> getPaymentsByOrderId(
            @PathVariable Long orderId
    ) {
        return paymentService.getPaymentsByOrderId(orderId);
    }
}