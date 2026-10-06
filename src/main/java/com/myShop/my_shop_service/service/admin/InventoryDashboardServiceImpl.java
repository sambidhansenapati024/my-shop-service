package com.myShop.my_shop_service.service.admin;


import com.myShop.my_shop_service.dto.admin.*;
import com.myShop.my_shop_service.repo.InventoryDashboardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InventoryDashboardServiceImpl
        implements InventoryDashboardService {

    private final InventoryDashboardRepository inventoryDashboardRepository;


    public InventoryDashboardServiceImpl(
            InventoryDashboardRepository inventoryDashboardRepository
    ) {
        this.inventoryDashboardRepository = inventoryDashboardRepository;
    }


    @Override
    public InventoryDashboardResponse getInventoryDashboard() {

        InventorySummaryResponse summary =
                buildSummary();

        List<LowStockProductResponse> lowStockProducts =
                buildLowStockProducts();

        InventoryValueResponse value =
                buildInventoryValue();

        List<ExpiringBatchResponse> expiringBatches =
                buildExpiringBatches(
                        inventoryDashboardRepository.getExpiringBatches()
                );

        List<ExpiringBatchResponse> expiredBatches =
                buildExpiringBatches(
                        inventoryDashboardRepository.getExpiredBatches()
                );

        return new InventoryDashboardResponse(
                summary,
                value,
                lowStockProducts,
                expiringBatches,
                expiredBatches
        );
    }

    /*
     * ---------------------------------------------------------
     * INVENTORY VALUE
     * ---------------------------------------------------------
     */
    private InventoryValueResponse buildInventoryValue() {

        List<Object[]> rows =
                inventoryDashboardRepository.getInventoryValue();

        if (rows == null || rows.isEmpty()) {

            return new InventoryValueResponse(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        Object[] row = rows.get(0);

        BigDecimal purchaseValue =
                toBigDecimal(row[0]);

        BigDecimal sellingValue =
                toBigDecimal(row[1]);

        BigDecimal potentialMargin =
                sellingValue.subtract(purchaseValue);

        BigDecimal marginPercentage =
                BigDecimal.ZERO;

        if (purchaseValue.compareTo(BigDecimal.ZERO) > 0) {

            marginPercentage =
                    potentialMargin
                            .divide(
                                    purchaseValue,
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP);
        }

        return new InventoryValueResponse(
                purchaseValue,
                sellingValue,
                potentialMargin,
                marginPercentage
        );
    }


    /*
     * ---------------------------------------------------------
     * SUMMARY
     * ---------------------------------------------------------
     */
    private InventorySummaryResponse buildSummary() {

        List<Object[]> rows =
                inventoryDashboardRepository.getInventorySummary();

        if (rows == null || rows.isEmpty()) {
            return new InventorySummaryResponse(
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L
            );
        }

        Object[] row = rows.get(0);

        return new InventorySummaryResponse(
                toLong(row[0]),
                toLong(row[1]),
                toLong(row[2]),
                toLong(row[3]),
                toLong(row[4]),
                toLong(row[5])
        );
    }


    /*
     * ---------------------------------------------------------
     * LOW STOCK PRODUCTS
     * ---------------------------------------------------------
     */
    private List<LowStockProductResponse> buildLowStockProducts() {

        List<Object[]> rows =
                inventoryDashboardRepository.getLowStockProducts();

        List<LowStockProductResponse> result =
                new ArrayList<>();

        for (Object[] row : rows) {

            if (row == null || row.length < 5) {
                continue;
            }

            result.add(
                    new LowStockProductResponse(
                            toLong(row[0]),
                            toStringValue(row[1]),
                            toStringValue(row[2]),
                            toStringValue(row[3]),
                            toBigDecimal(row[4])
                    )
            );
        }

        return result;
    }


    /*
     * ---------------------------------------------------------
     * EXPIRING / EXPIRED BATCHES
     * ---------------------------------------------------------
     */
    private List<ExpiringBatchResponse> buildExpiringBatches(
            List<Object[]> rows
    ) {

        List<ExpiringBatchResponse> result =
                new ArrayList<>();

        for (Object[] row : rows) {

            if (row == null || row.length < 8) {
                continue;
            }

            result.add(
                    new ExpiringBatchResponse(
                            toLong(row[0]),
                            toLong(row[1]),
                            toStringValue(row[2]),
                            toStringValue(row[3]),
                            toBigDecimal(row[4]),
                            toStringValue(row[5]),
                            toLocalDate(row[6]),
                            toLong(row[7])
                    )
            );
        }

        return result;
    }


    /*
     * ---------------------------------------------------------
     * CONVERSION HELPERS
     * ---------------------------------------------------------
     */

    private Long toLong(Object value) {

        if (value == null) {
            return 0L;
        }

        if (value instanceof Number number) {
            return number.longValue();
        }

        String text = value.toString().trim();

        if (text.isEmpty()) {
            return 0L;
        }

        return Long.parseLong(text);
    }


    private BigDecimal toBigDecimal(Object value) {

        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(value.toString());
    }


    private String toStringValue(Object value) {

        if (value == null) {
            return null;
        }

        return value.toString();
    }


    private LocalDate toLocalDate(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof LocalDate localDate) {
            return localDate;
        }

        return LocalDate.parse(value.toString());
    }
}