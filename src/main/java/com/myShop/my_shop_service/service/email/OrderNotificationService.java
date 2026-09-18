package com.myShop.my_shop_service.service.email;

import com.myShop.my_shop_service.entity.Order;

public interface OrderNotificationService {

    void processOrderCreated(Order order);

    void processBillGenerated(Order order);

    void processBillModified(Order order);

    void processPartialPayment(Order order);

    void processFullPayment(Order order);
}