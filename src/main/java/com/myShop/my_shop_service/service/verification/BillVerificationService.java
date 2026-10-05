package com.myShop.my_shop_service.service.verification;

import com.myShop.my_shop_service.dto.verification.BillVerificationResponse;
import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillVerificationService {

    private final OrderRepository orderRepository;

    public BillVerificationService(
            OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public BillVerificationResponse verifyBill(
            String verificationCode
    ) {

        if (verificationCode == null
                || verificationCode.isBlank()) {

            return new BillVerificationResponse(
                    false,
                    null,
                    null,
                    null,
                    null
            );
        }

        Order order =
                orderRepository
                        .findByVerificationCode(
                                verificationCode
                        )
                        .orElse(null);

        if (order == null) {

            return new BillVerificationResponse(
                    false,
                    null,
                    null,
                    null,
                    null
            );
        }

        return new BillVerificationResponse(
                true,
                "Senapati Store",
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getPaymentStatus()
        );
    }
}