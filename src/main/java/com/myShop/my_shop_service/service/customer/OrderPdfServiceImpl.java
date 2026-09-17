
package com.myShop.my_shop_service.service.customer;

import com.myShop.my_shop_service.dto.customer.OrderPdfData;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Service
public class OrderPdfServiceImpl implements OrderPdfService {

    private final TemplateEngine templateEngine;

    public OrderPdfServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] generateOrderPdf(OrderPdfData order) {

        return generatePdf(
                order,
                "order/order-template",
                "Failed to generate order PDF"
        );
    }

    @Override
    public byte[] generateBillPdf(OrderPdfData order) {

        return generatePdf(
                order,
                "order/bill-template",
                "Failed to generate bill PDF"
        );
    }

    private byte[] generatePdf(
            OrderPdfData order,
            String templateName,
            String errorMessage
    ) {

        Context context = new Context();

        context.setVariable("order", order);

        String html = templateEngine.process(
                templateName,
                context
        );

        try (ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    errorMessage,
                    e
            );
        }
    }
}