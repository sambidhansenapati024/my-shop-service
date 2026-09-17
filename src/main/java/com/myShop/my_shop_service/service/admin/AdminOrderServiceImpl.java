package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.admin.AdminOrderResponse;
import com.myShop.my_shop_service.dto.admin.CalculateBillResponse;
import com.myShop.my_shop_service.dto.admin.UpdateOrderStatusRequest;
import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.OrderItemResponse;
import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.User;
import com.myShop.my_shop_service.enums.OrderStatus;
import com.myShop.my_shop_service.repo.OrderItemRepository;
import com.myShop.my_shop_service.repo.OrderRepository;
import com.myShop.my_shop_service.service.email.OrderNotificationService;
import com.myShop.my_shop_service.service.s3Storage.S3StorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myShop.my_shop_service.dto.admin.CalculateBillRequest;
import com.myShop.my_shop_service.dto.admin.CalculateBillResponse;
import com.myShop.my_shop_service.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final S3StorageService s3StorageService;
    private final OrderNotificationService orderNotificationService;

    public AdminOrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            S3StorageService s3StorageService,
            OrderNotificationService orderNotificationService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.s3StorageService = s3StorageService;
        this.orderNotificationService = orderNotificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getAllOrders() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == null
                || !user.getRole().name().equals("ADMIN")) {

            return ApiResponse.error(
                    403,
                    "Only shopkeeper can view all orders"
            );
        }

        List<AdminOrderResponse> responses =
                orderRepository.findAll()
                        .stream()
                        .sorted(
                                (first, second) ->
                                        second.getCreatedAt()
                                                .compareTo(
                                                        first.getCreatedAt()
                                                )
                        )
                        .map(this::convertToResponse)
                        .toList();

        return ApiResponse.success(
                200,
                "Admin orders fetched successfully",
                responses
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getOrderById(Long orderId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == null
                || !user.getRole().name().equals("ADMIN")) {

            return ApiResponse.error(
                    403,
                    "Only shopkeeper can view order details"
            );
        }

        Order order =
                orderRepository.findById(orderId)
                        .orElse(null);

        if (order == null) {

            return ApiResponse.error(
                    404,
                    "Order not found"
            );
        }

        return ApiResponse.success(
                200,
                "Admin order fetched successfully",
                convertToResponse(order)
        );
    }

    private AdminOrderResponse convertToResponse(Order order) {

        AdminOrderResponse response =
                new AdminOrderResponse();

        response.setId(order.getId());

        response.setOrderNumber(
                order.getOrderNumber()
        );

        response.setUserId(
                order.getUser().getId()
        );

        response.setCustomerName(
                order.getUser().getName()
        );

        response.setOrderType(
                order.getOrderType()
        );

        response.setStatus(
                order.getStatus().name()
        );

        response.setPhotoPath(
                order.getPhotoPath()
        );

        if (order.getPhotoPath() != null
                && !order.getPhotoPath().isBlank()) {

            response.setPhotoUrl(
                    s3StorageService.generatePresignedUrl(
                            order.getPhotoPath()
                    )
            );
        }

        response.setPhotoNote(
                order.getPhotoNote()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        // BILLING FIELDS
        response.setTotalAmount(
                order.getTotalAmount()
        );

        response.setBilledAt(
                order.getBilledAt()
        );

        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrderId(order.getId())
                        .stream()
                        .map(item -> {

                            OrderItemResponse itemResponse =
                                    new OrderItemResponse(
                                            item.getId(),
                                            item.getItemName(),
                                            item.getQuantity(),
                                            item.getUnit()
                                    );

                            // BILLING FIELDS
                            itemResponse.setUnitPrice(
                                    item.getUnitPrice()
                            );

                            itemResponse.setItemTotal(
                                    item.getItemTotal()
                            );

                            return itemResponse;
                        })
                        .toList();

        response.setItems(items);

        response.setItemCount(
                items.size()
        );

        return response;
    }

    @Override
    @Transactional
    public ApiResponse<?> updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == null
                || !user.getRole().name().equals("ADMIN")) {

            return ApiResponse.error(
                    403,
                    "Only shopkeeper can update order status"
            );
        }

        Order order =
                orderRepository.findById(orderId)
                        .orElse(null);

        if (order == null) {

            return ApiResponse.error(
                    404,
                    "Order not found"
            );
        }

        order.setStatus(request.getStatus());

        Order updatedOrder =
                orderRepository.save(order);

        return ApiResponse.success(
                200,
                "Order status updated successfully",
                convertToResponse(updatedOrder)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> calculateBill(
            Long orderId,
            CalculateBillRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == null
                || !user.getRole().name().equals("ADMIN")) {

            return ApiResponse.error(
                    403,
                    "Only shopkeeper can calculate bills"
            );
        }

        Order order =
                orderRepository.findById(orderId)
                        .orElse(null);

        if (order == null) {

            return ApiResponse.error(
                    404,
                    "Order not found"
            );
        }

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        Map<Long, OrderItem> orderItemMap =
                orderItems.stream()
                        .collect(
                                Collectors.toMap(
                                        OrderItem::getId,
                                        Function.identity()
                                )
                        );

        Set<Long> requestedItemIds =
                request.getItems()
                        .stream()
                        .map(
                                CalculateBillRequest.ItemPriceRequest
                                        ::getItemId
                        )
                        .collect(Collectors.toSet());

        if (!orderItemMap.keySet().equals(requestedItemIds)) {

            return ApiResponse.error(
                    400,
                    "Prices must be provided for all order items"
            );
        }

        List<CalculateBillResponse.ItemCalculationResponse>
                calculatedItems = new java.util.ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (
                CalculateBillRequest.ItemPriceRequest priceRequest
                : request.getItems()
        ) {

            OrderItem item =
                    orderItemMap.get(priceRequest.getItemId());

            if (item == null) {

                return ApiResponse.error(
                        400,
                        "Invalid order item ID: "
                                + priceRequest.getItemId()
                );
            }

            BigDecimal unitPrice =
                    priceRequest.getUnitPrice();

            if (unitPrice == null
                    || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {

                return ApiResponse.error(
                        400,
                        "Unit price must be greater than zero"
                );
            }

            BigDecimal itemTotal =
                    calculateItemTotal(
                            item.getQuantity(),
                            item.getUnit(),
                            unitPrice
                    );

            calculatedItems.add(
                    new CalculateBillResponse.ItemCalculationResponse(
                            item.getId(),
                            item.getItemName(),
                            item.getQuantity(),
                            item.getUnit(),
                            unitPrice,
                            itemTotal
                    )
            );

            totalAmount =
                    totalAmount.add(itemTotal);
        }

        CalculateBillResponse response =
                new CalculateBillResponse(
                        calculatedItems,
                        totalAmount.setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                );

        return ApiResponse.success(
                200,
                "Bill calculated successfully",
                response
        );
    }

    private BigDecimal calculateItemTotal(
            BigDecimal quantity,
            String unit,
            BigDecimal unitPrice
    ) {

        String normalizedUnit =
                unit.trim().toLowerCase();

        BigDecimal multiplier;

        switch (normalizedUnit) {

            case "gm":
            case "g":
                // Convert grams to kilograms
                multiplier = quantity.divide(
                        BigDecimal.valueOf(1000),
                        6,
                        RoundingMode.HALF_UP
                );
                break;

            case "kg":
                multiplier = quantity;
                break;

            case "ml":
                // Convert millilitres to litres
                multiplier = quantity.divide(
                        BigDecimal.valueOf(1000),
                        6,
                        RoundingMode.HALF_UP
                );
                break;

            case "litre":
            case "l":
                multiplier = quantity;
                break;

            case "piece":
            case "packet":
            case "box":
            case "dozen":
                multiplier = quantity;
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported unit: " + unit
                );
        }

        return unitPrice
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public ApiResponse<?> generateBill(
            Long orderId,
            CalculateBillRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ApiResponse.error(
                    401,
                    "User is not authenticated"
            );
        }

        User user = (User) authentication.getPrincipal();

        if (user.getRole() == null
                || !user.getRole().name().equals("ADMIN")) {

            return ApiResponse.error(
                    403,
                    "Only shopkeeper can generate bills"
            );
        }

        Order order =
                orderRepository.findById(orderId)
                        .orElse(null);


        if (order == null) {

            return ApiResponse.error(
                    404,
                    "Order not found"
            );
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {

            return ApiResponse.error(
                    400,
                    "Order has Already Been Completed For this."
            );
        }


        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        Map<Long, OrderItem> orderItemMap =
                orderItems.stream()
                        .collect(
                                Collectors.toMap(
                                        OrderItem::getId,
                                        Function.identity()
                                )
                        );

        Set<Long> requestedItemIds =
                request.getItems()
                        .stream()
                        .map(
                                CalculateBillRequest.ItemPriceRequest
                                        ::getItemId
                        )
                        .collect(Collectors.toSet());

        if (!orderItemMap.keySet().equals(requestedItemIds)) {

            return ApiResponse.error(
                    400,
                    "Prices must be provided for all order items"
            );
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (
                CalculateBillRequest.ItemPriceRequest priceRequest
                : request.getItems()
        ) {

            OrderItem item =
                    orderItemMap.get(priceRequest.getItemId());

            if (item == null) {

                return ApiResponse.error(
                        400,
                        "Invalid order item ID: "
                                + priceRequest.getItemId()
                );
            }

            BigDecimal unitPrice =
                    priceRequest.getUnitPrice();

            if (unitPrice == null
                    || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {

                return ApiResponse.error(
                        400,
                        "Unit price must be greater than zero"
                );
            }

            BigDecimal itemTotal =
                    calculateItemTotal(
                            item.getQuantity(),
                            item.getUnit(),
                            unitPrice
                    );

            // Save unit price and item total
            item.setUnitPrice(unitPrice);
            item.setItemTotal(itemTotal);

            totalAmount = totalAmount.add(itemTotal);
        }

        totalAmount =
                totalAmount.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        // Save the order's total amount
        order.setTotalAmount(totalAmount);

        // Set billing timestamp
        order.setBilledAt(LocalDateTime.now());

        // Update order status
        // Set status based on whether this is a new bill or modification.
        // Check whether this is a bill modification
        boolean billModified =
                order.getStatus() == OrderStatus.BILLED
                        || order.getStatus() == OrderStatus.BILL_MODIFIED;

// Update order status
        if (billModified) {
            order.setStatus(OrderStatus.BILL_MODIFIED);
        }else{
            order.setStatus(OrderStatus.BILLED);
        }

        // Save all billing details
        orderItemRepository.saveAll(orderItems);
        Order savedOrder = orderRepository.save(order);

        if (billModified) {

            orderNotificationService.processBillModified(savedOrder);

        } else {

            orderNotificationService.processBillGenerated(savedOrder);

        }

        return ApiResponse.success(
                200,
                "Bill generated successfully",
                totalAmount
        );
    }
}