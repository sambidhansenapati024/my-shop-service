package com.myShop.my_shop_service.service.admin;



import com.myShop.my_shop_service.dto.PaymentResponse;
import com.myShop.my_shop_service.entity.Payment;
import com.myShop.my_shop_service.repo.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository
    ) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {

        List<Payment> payments =
                paymentRepository
                        .findByOrderIdOrderByPaymentDateDesc(orderId);

        return payments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }
}