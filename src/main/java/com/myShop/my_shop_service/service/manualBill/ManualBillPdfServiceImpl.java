package com.myShop.my_shop_service.service.manualBill;

import com.myShop.my_shop_service.dto.ManualBillPdfData;
import com.myShop.my_shop_service.dto.ManualBillPdfItem;
import com.myShop.my_shop_service.entity.ManualBill;
import com.myShop.my_shop_service.entity.ManualBillItem;
import com.myShop.my_shop_service.repo.ManualBillRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManualBillPdfServiceImpl implements ManualBillPdfService {

    private final TemplateEngine templateEngine;
    private final ManualBillRepository manualBillRepository;

    public ManualBillPdfServiceImpl(
            TemplateEngine templateEngine,
            ManualBillRepository manualBillRepository
    ) {
        this.templateEngine = templateEngine;
        this.manualBillRepository = manualBillRepository;
    }

    @Override
    public byte[] generateBillPdf(Long billId) {

        ManualBill bill = manualBillRepository.findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Manual bill not found with id: " + billId
                        )
                );

        List<ManualBillPdfItem> items = bill.getItems()
                .stream()
                .map(this::mapItem)
                .collect(Collectors.toList());

        ManualBillPdfData pdfData = new ManualBillPdfData(
                bill.getBillNumber(),
                bill.getCustomerName(),
                bill.getCustomerMobile(),
                bill.getSubtotal(),
                bill.getTotalAmount(),
                bill.getPaidAmount(),
                bill.getRemainingAmount(),
                bill.getPaymentStatus().name(),
                bill.getCreatedAt(),
                items
        );

        Context context = new Context();

        context.setVariable("bill", pdfData);

        String html = templateEngine.process(
                "order/manual-bill-template",
                context
        );

        try (
                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream()
        ) {

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate manual bill PDF",
                    e
            );
        }
    }

    private ManualBillPdfItem mapItem(ManualBillItem item) {

        return new ManualBillPdfItem(
                item.getItemName(),
                item.getQuantity(),
                item.getUnit(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}