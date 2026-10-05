package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.admin.AdminCustomerDetailsResponse;
import com.myShop.my_shop_service.dto.admin.AdminCustomerOrderResponse;
import com.myShop.my_shop_service.dto.admin.AdminCustomerPaymentResponse;
import com.myShop.my_shop_service.dto.admin.AdminCustomerResponse;
import com.myShop.my_shop_service.entity.Order;
import com.myShop.my_shop_service.entity.Payment;
import com.myShop.my_shop_service.entity.User;
import com.myShop.my_shop_service.enums.Role;
import com.myShop.my_shop_service.repo.OrderRepository;
import com.myShop.my_shop_service.repo.PaymentRepository;
import com.myShop.my_shop_service.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminCustomerServiceImpl
        implements AdminCustomerService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public AdminCustomerServiceImpl(
            UserRepository userRepository,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminCustomerResponse> getAllCustomers() {

        List<User> users =
                userRepository.findByRole(Role.USER);

        return users.stream()
                .map(user -> {

                    long totalOrders =
                            orderRepository.countByUserId(
                                    user.getId()
                            );

                    BigDecimal outstandingAmount =
                            orderRepository
                                    .getOutstandingAmountByUserId(
                                            user.getId()
                                    );

                    return new AdminCustomerResponse(
                            user.getId(),
                            user.getName(),
                            user.getMobileNumber(),
                            user.getEmail(),
                            totalOrders,
                            outstandingAmount
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCustomerDetailsResponse
    getCustomerDetails(Long customerId) {

        User customer =
                userRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );

        if (customer.getRole() != Role.USER) {
            throw new RuntimeException(
                    "The requested user is not a customer"
            );
        }

        List<Order> orders =
                orderRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                customerId
                        );

        List<Payment> payments =
                paymentRepository
                        .findByOrderUserIdOrderByPaymentDateDesc(
                                customerId
                        );

        BigDecimal outstandingAmount =
                orders.stream()
                        .map(Order::getRemainingAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        List<AdminCustomerOrderResponse>
                orderResponses =
                orders.stream()
                        .map(order ->
                                new AdminCustomerOrderResponse(
                                        order.getId(),
                                        order.getOrderNumber(),
                                        order.getOrderType(),
                                        order.getStatus(),
                                        order.getTotalAmount(),
                                        order.getPaidAmount(),
                                        order.getRemainingAmount(),
                                        order.getPaymentStatus(),
                                        order.getCreatedAt(),
                                        order.getBilledAt()
                                )
                        )
                        .toList();

        List<AdminCustomerPaymentResponse>
                paymentResponses =
                payments.stream()
                        .map(payment ->
                                new AdminCustomerPaymentResponse(
                                        payment.getId(),
                                        payment.getOrder()
                                                .getOrderNumber(),
                                        payment.getAmount(),
                                        payment.getPaymentMethod(),
                                        payment.getPaymentDate()
                                )
                        )
                        .toList();

        AdminCustomerDetailsResponse response =
                new AdminCustomerDetailsResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setMobileNumber(
                customer.getMobileNumber()
        );
        response.setEmail(customer.getEmail());

        response.setTotalOrders(
                (long) orders.size()
        );

        response.setOutstandingAmount(
                outstandingAmount
        );

        response.setOrders(orderResponses);
        response.setPayments(paymentResponses);

        return response;
    }
}