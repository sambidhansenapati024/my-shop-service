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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

            context.setVariable(
                    "customerName",
                    customerName
            );

            context.setVariable(
                    "orderNumber",
                    orderNumber
            );

            context.setVariable(
                    "orderType",
                    orderType
            );

            context.setVariable(
                    "status",
                    status
            );

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

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setTo(to);

            helper.setSubject(
                    "My Shop - Order " + orderNumber
            );

            helper.setText(
                    emailBody,
                    true
            );

            helper.addAttachment(
                    orderNumber + ".pdf",
                    new ByteArrayResource(pdfBytes)
            );

            mailSender.send(message);

            System.out.println(
                    "Order email sent successfully to: "
                            + to
            );

        } catch (MessagingException e) {

            System.err.println(
                    "Failed to send order email to: "
                            + to
            );

            e.printStackTrace();
        }
    }
}