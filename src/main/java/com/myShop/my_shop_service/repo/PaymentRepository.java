package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderIdOrderByPaymentDateDesc(Long orderId);

}