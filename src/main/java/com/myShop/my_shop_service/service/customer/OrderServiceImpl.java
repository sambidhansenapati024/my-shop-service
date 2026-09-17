package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.auth.ApiResponse;
import com.myShop.my_shop_service.dto.customer.CreateOrderRequest;
import com.myShop.my_shop_service.dto.customer.OrderItemResponse;
import com.myShop.my_shop_service.dto.customer.OrderPdfData;
import com.myShop.my_shop_service.dto.customer.OrderResponse;
import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.OrderItem;
import com.myShop.my_shop_service.entity.User;
import com.myShop.my_shop_service.enums.OrderStatus;
import com.myShop.my_shop_service.repo.OrderItemRepository;
import com.myShop.my_shop_service.repo.OrderRepository;
import com.myShop.my_shop_service.service.email.EmailService;
import com.myShop.my_shop_service.service.email.OrderNotificationService;
import com.myShop.my_shop_service.service.s3Storage.S3StorageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final S3StorageService s3StorageService;
    private final OrderPdfService orderPdfService;
    private final EmailService emailService;
    private final OrderNotificationService orderNotificationService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            S3StorageService s3StorageService,
            OrderPdfService orderPdfService,
            EmailService emailService,
            OrderNotificationService orderNotificationService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.s3StorageService = s3StorageService;
        this.orderPdfService = orderPdfService;
        this.emailService = emailService;
        this.orderNotificationService = orderNotificationService;
    }

    @Override
    @Transactional
    public ApiResponse<?> createOrder(
            CreateOrderRequest request,
            MultipartFile photo
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

        String orderType = request.getOrderType()
                .trim()
                .toUpperCase();

        if (!orderType.equals("MANUAL")
                && !orderType.equals("PHOTO")) {

            return ApiResponse.error(
                    400,
                    "Order type must be MANUAL or PHOTO"
            );
        }

        /*
         * MANUAL ORDER VALIDATION
         */
        if (orderType.equals("MANUAL")) {

            if (request.getItems() == null
                    || request.getItems().isEmpty()) {

                return ApiResponse.error(
                        400,
                        "At least one item is required for a manual order"
                );
            }

            /*
             * Manual order must not contain a photo.
             */
            if (photo != null && !photo.isEmpty()) {

                return ApiResponse.error(
                        400,
                        "Photo is not allowed for a manual order"
                );
            }
        }

        /*
         * PHOTO ORDER VALIDATION
         */
        if (orderType.equals("PHOTO")) {

            if (photo == null || photo.isEmpty()) {

                return ApiResponse.error(
                        400,
                        "Photo is required for a photo order"
                );
            }

            if (request.getPhotoNote() != null
                    && request.getPhotoNote().length() > 1000) {

                return ApiResponse.error(
                        400,
                        "Photo note must not exceed 1000 characters"
                );
            }
        }

        /*
         * Create order
         */
        Order order = new Order();

        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setOrderType(orderType);
        order.setStatus(OrderStatus.RECEIVED);
        order.setPhotoNote(request.getPhotoNote());
        order.setCreatedAt(LocalDateTime.now());

        /*
         * Upload photo to S3
         */
        if (orderType.equals("PHOTO")) {

            try {

                String photoPath =
                        s3StorageService.uploadFile(
                                photo,
                                "orders"
                        );

                order.setPhotoPath(photoPath);

            } catch (IOException e) {

                return ApiResponse.error(
                        500,
                        "Failed to upload order photo"
                );
            }
        }

        Order savedOrder = orderRepository.save(order);

        /*
         * Save manual order items
         */
        if (orderType.equals("MANUAL")) {

            List<OrderItem> orderItems = new ArrayList<>();

            request.getItems().forEach(itemRequest -> {

                OrderItem orderItem = new OrderItem();

                orderItem.setOrder(savedOrder);
                orderItem.setItemName(
                        itemRequest.getItemName().trim()
                );
                orderItem.setQuantity(
                        itemRequest.getQuantity()
                );
                orderItem.setUnit(
                        itemRequest.getUnit().trim()
                );

                orderItems.add(orderItem);
            });

            orderItemRepository.saveAll(orderItems);
        }

        OrderResponse response =
                convertToResponse(savedOrder);

        orderNotificationService.processOrderCreated(order);

        return ApiResponse.success(
                201,
                "Order placed successfully",
                response
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<?> getMyOrders() {

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

        List<Order> orders =
                orderRepository.findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                );

        List<OrderResponse> responses =
                orders.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ApiResponse.success(
                200,
                "Orders fetched successfully",
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

        System.out.println("orderid---"+orderId);
        System.out.println("userid"+user.getId());

        Order order =
                orderRepository.findByIdAndUserId(
                        orderId,
                        user.getId()
                ).orElse(null);

        if (order == null) {

            return ApiResponse.error(
                    404,
                    "Order not found"
            );
        }

        return ApiResponse.success(
                200,
                "Order fetched successfully",
                convertToResponse(order)
        );
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

        List<Order> orders =
                orderRepository.findAll();

        orders.sort(
                (first, second) ->
                        second.getCreatedAt()
                                .compareTo(first.getCreatedAt())
        );

        List<OrderResponse> responses =
                orders.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ApiResponse.success(
                200,
                "Orders fetched successfully",
                responses
        );
    }

    private String generateOrderNumber() {

        long nextId =
                orderRepository.count() + 1001;

        String orderNumber =
                "ORD-" + nextId;

        while (orderRepository.existsByOrderNumber(orderNumber)) {

            nextId++;

            orderNumber =
                    "ORD-" + nextId;
        }

        return orderNumber;
    }

    private OrderResponse convertToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());

        response.setOrderNumber(order.getOrderNumber());

        response.setUserId(order.getUser().getId());

        response.setOrderType(order.getOrderType());

        response.setStatus(order.getStatus().name());

        response.setPhotoPath(order.getPhotoPath());

        if (order.getPhotoPath() != null
                && !order.getPhotoPath().isBlank()) {

            response.setPhotoUrl(
                    s3StorageService.generatePresignedUrl(
                            order.getPhotoPath()
                    )
            );
        }

        response.setPhotoNote(order.getPhotoNote());

        response.setCreatedAt(order.getCreatedAt());


        // BILLING FIELDS

        response.setTotalAmount(order.getTotalAmount());

        response.setBilledAt(order.getBilledAt());

        // PAYMENT FIELDS

        response.setPaymentStatus(
                order.getPaymentStatus()
        );

        response.setPaidAmount(
                order.getPaidAmount()
        );

        response.setRemainingAmount(
                order.getRemainingAmount()
        );


        // ORDER ITEMS

        List<OrderItemResponse> itemResponses =
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


        response.setItems(itemResponses);

        return response;
    }

    private OrderPdfData convertToPdfData(
            Order order,
            List<OrderItemResponse> items
    ) {
        OrderPdfData pdfData = new OrderPdfData();

        pdfData.setOrderNumber(order.getOrderNumber());
        pdfData.setCustomerName(order.getUser().getName());
        pdfData.setCreatedAt(order.getCreatedAt());
        pdfData.setOrderType(order.getOrderType());
        pdfData.setStatus(order.getStatus().name());
        pdfData.setItems(items);

        return pdfData;
    }
}