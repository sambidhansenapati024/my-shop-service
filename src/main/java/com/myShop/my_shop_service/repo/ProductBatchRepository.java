package com.myShop.my_shop_service.repository;

import com.myShop.my_shop_service.entity.ProductBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductBatchRepository
        extends JpaRepository<ProductBatch, Long> {

    List<ProductBatch> findByProductId(Long productId);

    Optional<ProductBatch> findByProductIdAndBatchNumber(
            Long productId,
            String batchNumber
    );

    List<ProductBatch> findByProductIdAndQuantityGreaterThan(
            Long productId,
            java.math.BigDecimal quantity
    );
}