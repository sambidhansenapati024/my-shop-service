package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.ManualBillItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManualBillItemRepository
        extends JpaRepository<ManualBillItem, Long> {

    List<ManualBillItem> findByBillId(Long billId);
}