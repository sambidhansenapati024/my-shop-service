package com.myShop.my_shop_service.service.email;

import com.myShop.my_shop_service.dto.customer.OrderItemResponse;
import com.myShop.my_shop_service.dto.customer.OrderPdfData;
import com.myShop.my_shop_service.dto.customer.OrderResponse;
import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.OrderItem;
import com.myShop.my_shop_service.repo.OrderItemRepository;
import com.myShop.my_shop_service.service.customer.OrderPdfService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderNotificationServiceImpl
        implements OrderNotificationService {

    private final OrderPdfService orderPdfService;
    private final EmailService emailService;
    private final OrderItemRepository orderItemRepository;

    public OrderNotificationServiceImpl(
            OrderPdfService orderPdfService,
            EmailService emailService,
            OrderItemRepository orderItemRepository
    ) {
        this.orderPdfService = orderPdfService;
        this.emailService = emailService;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Async
    public void processOrderCreated(Order order) {

        try {

            OrderPdfData pdfData = new OrderPdfData();

            pdfData.setOrderNumber(
                    order.getOrderNumber()
            );

            pdfData.setCustomerName(
                    order.getUser().getName()
            );

            pdfData.setCreatedAt(
                    order.getCreatedAt()
            );

            pdfData.setOrderType(
                    order.getOrderType()
            );

            pdfData.setStatus(
                    order.getStatus().name()
            );

            List<OrderItem> orderItems =
                    orderItemRepository.findByOrderId(
                            order.getId()
                    );

            List<OrderItemResponse> itemResponses =
                    orderItems.stream()
                            .map(item -> {

                                OrderItemResponse response =
                                        new OrderItemResponse();

                                response.setItemName(
                                        item.getItemName()
                                );

                                response.setQuantity(
                                        item.getQuantity()
                                );

                                response.setUnit(
                                        item.getUnit()
                                );

                                return response;
                            })
                            .toList();

            pdfData.setItems(itemResponses);

            byte[] pdfBytes =
                    orderPdfService.generateOrderPdf(pdfData);

            System.out.println(
                    "Async order PDF generated. Size: "
                            + pdfBytes.length
                            + " bytes"
            );

            emailService.sendOrderEmail(
                    order.getUser().getEmail(),
                    order.getUser().getName(),
                    order.getOrderNumber(),
                    order.getOrderType(),
                    order.getStatus().name(),
                    order.getCreatedAt(),
                    pdfBytes
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to process order notification for: "
                            + order.getOrderNumber()
            );

            e.printStackTrace();
        }
    }
}