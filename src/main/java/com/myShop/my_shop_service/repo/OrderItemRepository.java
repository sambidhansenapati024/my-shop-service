package com.myShop.my_shop_service.repo;

import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
}
