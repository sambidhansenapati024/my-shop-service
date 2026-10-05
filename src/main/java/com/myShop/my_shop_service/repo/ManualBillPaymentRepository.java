package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.ManualBillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManualBillPaymentRepository
        extends JpaRepository<ManualBillPayment, Long> {

    List<ManualBillPayment> findByBillIdOrderByPaidAtDesc(Long billId);
}