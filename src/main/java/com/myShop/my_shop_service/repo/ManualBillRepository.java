package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.ManualBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManualBillRepository
        extends JpaRepository<ManualBill, Long> {

    boolean existsByBillNumber(String billNumber);

    Optional<ManualBill> findByBillNumber(String billNumber);
}