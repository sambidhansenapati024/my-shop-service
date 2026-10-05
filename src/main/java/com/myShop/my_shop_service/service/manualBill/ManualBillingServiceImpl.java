package com.myShop.my_shop_service.service.manualBill;

import com.myShop.my_shop_service.dto.ManualBillPaymentRequest;
import com.myShop.my_shop_service.dto.ManualBillPaymentResponse;
import com.myShop.my_shop_service.dto.admin.*;
import com.myShop.my_shop_service.entity.*;
import com.myShop.my_shop_service.enums.PaymentStatus;
import com.myShop.my_shop_service.repo.ManualBillItemRepository;
import com.myShop.my_shop_service.repo.ManualBillPaymentRepository;
import com.myShop.my_shop_service.repo.ManualBillRepository;
import com.myShop.my_shop_service.repo.ProductRepository;
import com.myShop.my_shop_service.repository.ProductBatchRepository;
import com.myShop.my_shop_service.service.inventory.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class ManualBillingServiceImpl
        implements ManualBillingService {

    private final ManualBillRepository manualBillRepository;
    private final ManualBillItemRepository manualBillItemRepository;
    private final ProductRepository productRepository;
    private final ProductBatchRepository productBatchRepository;
    private final InventoryService inventoryService;
    private final ManualBillPaymentRepository manualBillPaymentRepository;

    public ManualBillingServiceImpl(
            ManualBillRepository manualBillRepository,
            ManualBillItemRepository manualBillItemRepository,
            ProductRepository productRepository,
            ProductBatchRepository productBatchRepository,
            InventoryService inventoryService,
            ManualBillPaymentRepository manualBillPaymentRepository
    ) {
        this.manualBillRepository = manualBillRepository;
        this.manualBillItemRepository = manualBillItemRepository;
        this.productRepository = productRepository;
        this.productBatchRepository = productBatchRepository;
        this.inventoryService = inventoryService;
        this.manualBillPaymentRepository = manualBillPaymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findProductByBarcode(
            String barcode
    ) {
        return inventoryService.findProductByBarcode(barcode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBatchResponse> getAvailableBatches(
            Long productId
    ) {

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException(
                    "Product not found"
            );
        }

        return productBatchRepository
                .findByProductIdAndQuantityGreaterThan(
                        productId,
                        BigDecimal.ZERO
                )
                .stream()
                .map(this::toProductBatchResponse)
                .toList();
    }

    @Override
    public ManualBillResponse createBill(
            CreateManualBillRequest request
    ) {

        ManualBill bill = new ManualBill();

        bill.setBillNumber(
                generateUniqueBillNumber()
        );

        bill.setCustomerName(
                normalize(request.getCustomerName())
        );

        bill.setCustomerMobile(
                normalize(request.getCustomerMobile())
        );

        bill.setSubtotal(BigDecimal.ZERO);
        bill.setTotalAmount(BigDecimal.ZERO);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setRemainingAmount(BigDecimal.ZERO);
        bill.setPaymentStatus(PaymentStatus.UNPAID);

        ManualBill savedBill =
                manualBillRepository.save(bill);

        return toManualBillResponse(savedBill);
    }

    @Override
    @Transactional(readOnly = true)
    public ManualBillResponse getBill(
            Long billId
    ) {

        ManualBill bill =
                getBillEntity(billId);

        return toManualBillResponse(bill);
    }

    @Override
    public ManualBillResponse addItem(
            Long billId,
            ManualBillItemRequest request
    ) {

        ManualBill bill =
                getBillEntity(billId);

        ensureBillIsEditable(bill);

        Product product =
                productRepository
                        .findById(request.getProductId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found"
                                )
                        );

        ProductBatch productBatch =
                productBatchRepository
                        .findById(request.getProductBatchId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product batch not found"
                                )
                        );

        /*
         * Make sure the selected batch actually
         * belongs to the selected product.
         */
        if (!productBatch.getProduct().getId()
                .equals(product.getId())) {

            throw new IllegalArgumentException(
                    "Selected batch does not belong to the product"
            );
        }

        BigDecimal requestedQuantity =
                request.getQuantity();

        BigDecimal availableQuantity =
                productBatch.getQuantity();

        /*
         * Check how much of this batch has already
         * been added to the current bill.
         */
        BigDecimal alreadyAddedQuantity =
                bill.getItems()
                        .stream()
                        .filter(item ->
                                item.getProductBatch() != null
                                        && item.getProductBatch()
                                        .getId()
                                        .equals(
                                                productBatch.getId()
                                        )
                        )
                        .map(ManualBillItem::getQuantity)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal remainingAvailableQuantity =
                availableQuantity
                        .subtract(alreadyAddedQuantity);

        if (requestedQuantity.compareTo(
                remainingAvailableQuantity
        ) > 0) {

            throw new IllegalArgumentException(
                    "Insufficient stock. Available quantity: "
                            + remainingAvailableQuantity
            );
        }

        BigDecimal unitPrice =
                productBatch.getSellingPrice();

        BigDecimal totalPrice =
                unitPrice
                        .multiply(requestedQuantity)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        ManualBillItem item =
                new ManualBillItem();

        item.setProduct(product);
        item.setProductBatch(productBatch);

        item.setItemName(
                product.getProductName()
        );

        item.setQuantity(
                requestedQuantity
        );

        item.setUnit(
                product.getUnit()
        );

        item.setUnitPrice(
                unitPrice
        );

        item.setTotalPrice(
                totalPrice
        );

        /*
         * Do not reduce inventory here.
         *
         * Inventory will be reduced when the bill
         * is finally completed/paid.
         */
        bill.addItem(item);

        manualBillItemRepository.save(item);

        recalculateBill(bill);

        manualBillRepository.save(bill);

        return toManualBillResponse(bill);
    }

    @Override
    public ManualBillResponse removeItem(
            Long billId,
            Long itemId
    ) {

        ManualBill bill =
                getBillEntity(billId);

        ManualBillItem item =
                manualBillItemRepository
                        .findById(itemId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Bill item not found"
                                )
                        );
        ensureBillIsEditable(bill);

        if (!item.getBill()
                .getId()
                .equals(bill.getId())) {

            throw new IllegalArgumentException(
                    "Bill item does not belong to this bill"
            );
        }

        bill.removeItem(item);

        manualBillItemRepository.delete(item);

        recalculateBill(bill);

        manualBillRepository.save(bill);

        return toManualBillResponse(bill);
    }

    private void recalculateBill(
            ManualBill bill
    ) {

        BigDecimal subtotal =
                bill.getItems()
                        .stream()
                        .map(ManualBillItem::getTotalPrice)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        bill.setSubtotal(subtotal);

        bill.setTotalAmount(subtotal);

        BigDecimal paidAmount =
                bill.getPaidAmount() != null
                        ? bill.getPaidAmount()
                        : BigDecimal.ZERO;

        BigDecimal remainingAmount =
                subtotal.subtract(paidAmount);

        if (remainingAmount.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            remainingAmount =
                    BigDecimal.ZERO;
        }

        bill.setRemainingAmount(
                remainingAmount
        );

        updatePaymentStatus(bill);
    }

    private void updatePaymentStatus(
            ManualBill bill
    ) {

        BigDecimal total =
                bill.getTotalAmount();

        BigDecimal paid =
                bill.getPaidAmount() != null
                        ? bill.getPaidAmount()
                        : BigDecimal.ZERO;

        if (paid.compareTo(BigDecimal.ZERO) <= 0) {

            bill.setPaymentStatus(
                    PaymentStatus.UNPAID
            );

        } else if (paid.compareTo(total) >= 0) {

            bill.setPaymentStatus(
                    PaymentStatus.PAID
            );

        } else {

            bill.setPaymentStatus(
                    PaymentStatus.PARTIAL
            );
        }
    }

    private ManualBill getBillEntity(
            Long billId
    ) {

        return manualBillRepository
                .findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Manual bill not found"
                        )
                );
    }

    private ManualBillResponse toManualBillResponse(
            ManualBill bill
    ) {

        List<ManualBillItemResponse> items =
                bill.getItems()
                        .stream()
                        .map(this::toManualBillItemResponse)
                        .toList();

        ManualBillResponse response =
                new ManualBillResponse(
                        bill.getId(),
                        bill.getBillNumber(),
                        bill.getCustomerName(),
                        bill.getCustomerMobile(),
                        bill.getSubtotal(),
                        bill.getTotalAmount(),
                        bill.getPaidAmount(),
                        bill.getRemainingAmount(),
                        bill.getPaymentStatus(),
                        bill.getCreatedAt(),
                        bill.getUpdatedAt(),
                        items
                );

        response.setBilledAt(bill.getBilledAt());

        return response;
    }

    private ManualBillItemResponse
    toManualBillItemResponse(
            ManualBillItem item
    ) {

        return new ManualBillItemResponse(
                item.getId(),

                item.getProduct() != null
                        ? item.getProduct().getId()
                        : null,

                item.getProductBatch() != null
                        ? item.getProductBatch().getId()
                        : null,

                item.getItemName(),
                item.getQuantity(),
                item.getUnit(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }

    private ProductBatchResponse
    toProductBatchResponse(
            ProductBatch batch
    ) {

        return new ProductBatchResponse(
                batch.getId(),
                batch.getProduct().getId(),
                batch.getBatchNumber(),
                batch.getPurchasePrice(),
                batch.getSellingPrice(),
                batch.getQuantity(),
                batch.getExpiryDate()
        );
    }

    private String generateUniqueBillNumber() {

        String billNumber;

        do {

            String date =
                    LocalDate.now()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "yyyyMMdd"
                                            )
                            );

            int randomNumber =
                    ThreadLocalRandom.current()
                            .nextInt(
                                    1000,
                                    10000
                            );

            billNumber =
                    "MB-"
                            + date
                            + "-"
                            + randomNumber;

        } while (
                manualBillRepository
                        .existsByBillNumber(
                                billNumber
                        )
        );

        return billNumber;
    }

    private String normalize(
            String value
    ) {

        if (value == null) {
            return null;
        }

        String trimmed =
                value.trim();

        return trimmed.isEmpty()
                ? null
                : trimmed;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @Override
    @Transactional
    public ManualBillResponse processPayment(
            Long billId,
            ManualBillPaymentRequest request
    ) {
        ManualBill bill = manualBillRepository.findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Manual bill not found."));

        ensureBillIsEditable(bill);

        if (request == null || request.getPaidAmount() == null) {
            throw new IllegalArgumentException("Payment amount is required.");
        }

        BigDecimal paymentAmount = request.getPaidAmount();

        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero."
            );
        }

        if (request.getPaymentMethod() == null
                || request.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException(
                    "Payment method is required."
            );
        }

        BigDecimal remainingAmount = bill.getRemainingAmount();

        if (paymentAmount.compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException(
                    "Payment amount cannot be greater than remaining amount."
            );
        }

        BigDecimal alreadyPaid = bill.getPaidAmount() != null
                ? bill.getPaidAmount()
                : BigDecimal.ZERO;

        BigDecimal newPaidAmount =
                alreadyPaid.add(paymentAmount);

        BigDecimal newRemainingAmount =
                bill.getTotalAmount().subtract(newPaidAmount);

        if (newRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            newRemainingAmount = BigDecimal.ZERO;
        }

        PaymentStatus previousStatus = bill.getPaymentStatus();

        bill.setPaidAmount(newPaidAmount);
        bill.setRemainingAmount(newRemainingAmount);

        updatePaymentStatus(bill);

        /*
         * If this payment completes the bill,
         * deduct inventory.
         */
        if (bill.getPaymentStatus() == PaymentStatus.PAID
                && previousStatus != PaymentStatus.PAID) {

            deductInventory(bill);
        }

        /*
         * Save the individual payment transaction.
         */
        ManualBillPayment payment = new ManualBillPayment(
                bill,
                paymentAmount,
                request.getPaymentMethod().trim().toUpperCase()
        );

        manualBillPaymentRepository.save(payment);

        ManualBill savedBill =
                manualBillRepository.save(bill);

        return toManualBillResponse(savedBill);
    }

    private void deductInventory(ManualBill bill) {

        Map<Long, BigDecimal> quantityByBatch =
                new HashMap<>();

        /*
         * First calculate the total quantity required
         * from each batch.
         */
        for (ManualBillItem item : bill.getItems()) {

            if (item.getProductBatch() == null) {
                throw new IllegalArgumentException(
                        "Product batch is missing for item: "
                                + item.getItemName()
                );
            }

            Long batchId =
                    item.getProductBatch().getId();

            quantityByBatch.merge(
                    batchId,
                    item.getQuantity(),
                    BigDecimal::add
            );
        }

        /*
         * Check stock before changing anything.
         */
        for (Map.Entry<Long, BigDecimal> entry
                : quantityByBatch.entrySet()) {

            Long batchId = entry.getKey();

            BigDecimal requiredQuantity =
                    entry.getValue();

            ProductBatch batch =
                    productBatchRepository
                            .findById(batchId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Product batch not found: "
                                                    + batchId
                                    )
                            );

            BigDecimal availableQuantity =
                    batch.getQuantity();

            if (availableQuantity.compareTo(
                    requiredQuantity
            ) < 0) {

                throw new IllegalArgumentException(
                        "Insufficient stock for batch "
                                + batch.getBatchNumber()
                                + ". Available: "
                                + availableQuantity
                                + ", Required: "
                                + requiredQuantity
                );
            }
        }

        /*
         * Now deduct the inventory.
         */
        for (Map.Entry<Long, BigDecimal> entry
                : quantityByBatch.entrySet()) {

            Long batchId = entry.getKey();

            BigDecimal requiredQuantity =
                    entry.getValue();

            ProductBatch batch =
                    productBatchRepository
                            .findById(batchId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Product batch not found: "
                                                    + batchId
                                    )
                            );

            batch.setQuantity(
                    batch.getQuantity()
                            .subtract(requiredQuantity)
            );

            productBatchRepository.save(batch);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManualBillPaymentResponse> getPaymentHistory(Long billId) {

        manualBillRepository.findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Manual bill not found."));

        return manualBillPaymentRepository
                .findByBillIdOrderByPaidAtDesc(billId)
                .stream()
                .map(payment -> new ManualBillPaymentResponse(
                        payment.getId(),
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getPaidAt()
                ))
                .toList();
    }

    private void ensureBillIsEditable(ManualBill bill) {

        if (bill.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException(
                    "Paid bill cannot be modified."
            );
        }
    }

    @Override
    public ManualBillResponse generateBill(Long billId) {

        ManualBill bill = manualBillRepository.findById(billId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Manual bill not found.")
                );

        ensureBillHasItems(bill);

        if (bill.getBilledAt() != null) {
            throw new IllegalArgumentException("Bill has already been generated.");
        }

        bill.setBilledAt(LocalDateTime.now());

        ManualBill savedBill = manualBillRepository.save(bill);

        return toManualBillResponse(savedBill);
    }

    private void ensureBillHasItems(ManualBill bill) {

        if (bill.getItems() == null || bill.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot generate bill without any items."
            );
        }
    }
}