package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    boolean existsByOrderNumber(String orderNumber);

    Optional<Order> findByVerificationCode(String verificationCode);

    boolean existsByVerificationCode(String verificationCode);

    long countByUserId(Long userId);

    @Query("""
        SELECT COALESCE(SUM(o.remainingAmount), 0)
        FROM Order o
        WHERE o.user.id = :userId
    """)
    BigDecimal getOutstandingAmountByUserId(
            @Param("userId") Long userId
    );
}
