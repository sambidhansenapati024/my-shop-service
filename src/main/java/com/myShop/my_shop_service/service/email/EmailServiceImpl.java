
package com.myShop.my_shop_service.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            TemplateEngine templateEngine
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendOrderEmail(
            String to,
            String customerName,
            String orderNumber,
            String orderType,
            String status,
            LocalDateTime createdAt,
            byte[] pdfBytes
    ) {

        try {

            Context context = new Context();

            context.setVariable("customerName", customerName);
            context.setVariable("orderNumber", orderNumber);
            context.setVariable("orderType", orderType);
            context.setVariable("status", status);

            context.setVariable(
                    "createdAt",
                    createdAt.format(
                            DateTimeFormatter.ofPattern(
                                    "dd MMMM yyyy, hh:mm a"
                            )
                    )
            );

            String emailBody =
                    templateEngine.process(
                            "order/email/order-email",
                            context
                    );

            sendEmail(
                    to,
                    "My Shop - Order " + orderNumber,
                    emailBody,
                    orderNumber + ".pdf",
                    pdfBytes
            );

            System.out.println(
                    "Order email sent successfully to: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send order email to: " + to
            );

            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendBillGeneratedEmail(
            String to,
            String customerName,
            String orderNumber,
            byte[] pdfBytes
    ) {

        try {

            Context context = new Context();

            context.setVariable("customerName", customerName);
            context.setVariable("orderNumber", orderNumber);

            String emailBody =
                    templateEngine.process(
                            "order/email/bill-generated-email",
                            context
                    );

            sendEmail(
                    to,
                    "My Shop - Bill Generated for Order " + orderNumber,
                    emailBody,
                    orderNumber + ".pdf",
                    pdfBytes
            );

            System.out.println(
                    "Bill generated email sent successfully to: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send bill generated email to: " + to
            );

            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendBillModifiedEmail(
            String to,
            String customerName,
            String orderNumber,
            byte[] pdfBytes
    ) {

        try {

            Context context = new Context();

            context.setVariable("customerName", customerName);
            context.setVariable("orderNumber", orderNumber);

            String emailBody =
                    templateEngine.process(
                            "order/email/bill-modified-email",
                            context
                    );

            sendEmail(
                    to,
                    "My Shop - Bill Modified for Order " + orderNumber,
                    emailBody,
                    orderNumber + ".pdf",
                    pdfBytes
            );

            System.out.println(
                    "Bill modified email sent successfully to: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send bill modified email to: " + to
            );

            e.printStackTrace();
        }
    }

    private void sendEmail(
            String to,
            String subject,
            String emailBody,
            String attachmentName,
            byte[] pdfBytes
    ) throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        true,
                        "UTF-8"
                );

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailBody, true);

        helper.addAttachment(
                attachmentName,
                new ByteArrayResource(pdfBytes)
        );

        mailSender.send(message);
    }

    @Override
    @Async
    public void sendPartialPaymentEmail(
            String to,
            String customerName,
            String orderNumber,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            byte[] pdfBytes
    ) {

        try {

            Context context = new Context();

            context.setVariable("customerName", customerName);

            context.setVariable(
                    "order",
                    createPaymentEmailData(
                            orderNumber,
                            totalAmount,
                            paidAmount,
                            remainingAmount,
                            "PARTIAL"
                    )
            );

            String emailBody =
                    templateEngine.process(
                            "order/email/partial-payment",
                            context
                    );

            sendEmail(
                    to,
                    "My Shop - Partial Payment for Order " + orderNumber,
                    emailBody,
                    orderNumber + "-partial-payment.pdf",
                    pdfBytes
            );

            System.out.println(
                    "Partial payment email sent successfully to: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send partial payment email to: " + to
            );

            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendFullPaymentEmail(
            String to,
            String customerName,
            String orderNumber,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            byte[] pdfBytes
    ) {

        try {

            Context context = new Context();

            context.setVariable("customerName", customerName);

            context.setVariable(
                    "order",
                    createPaymentEmailData(
                            orderNumber,
                            totalAmount,
                            paidAmount,
                            remainingAmount,
                            "PAID"
                    )
            );

            String emailBody =
                    templateEngine.process(
                            "order/email/full-payment",
                            context
                    );

            sendEmail(
                    to,
                    "My Shop - Full Payment Received for Order " + orderNumber,
                    emailBody,
                    orderNumber + "-full-payment.pdf",
                    pdfBytes
            );

            System.out.println(
                    "Full payment email sent successfully to: " + to
            );

        } catch (Exception e) {

            System.err.println(
                    "Failed to send full payment email to: " + to
            );

            e.printStackTrace();
        }
    }

    private Map<String, Object> createPaymentEmailData(
            String orderNumber,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal remainingAmount,
            String paymentStatus
    ) {

        Map<String, Object> orderData = new HashMap<>();

        orderData.put("orderNumber", orderNumber);
        orderData.put("totalAmount", totalAmount);
        orderData.put("paidAmount", paidAmount);
        orderData.put("remainingAmount", remainingAmount);
        orderData.put("paymentStatus", paymentStatus);

        return orderData;
    }
}