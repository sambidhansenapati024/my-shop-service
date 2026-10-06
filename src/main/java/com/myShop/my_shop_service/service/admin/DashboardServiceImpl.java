package com.myShop.my_shop_service.service.admin;

import com.myShop.my_shop_service.dto.admin.*;
import com.myShop.my_shop_service.repo.DashboardRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(
            DashboardRepository dashboardRepository
    ) {
        this.dashboardRepository = dashboardRepository;
    }


    @Override
    public DashboardResponse getDashboard() {

        DashboardSummaryResponse summary =
                buildSummary();

        List<MonthlySalesResponse> monthlySales =
                buildMonthlySales();

        List<TopSellingItemResponse> topSellingItems =
                buildTopSellingItems();

        PaymentAnalysisResponse paymentAnalysis =
                buildPaymentAnalysis();

        OrderAnalysisResponse orderAnalysis =
                buildOrderAnalysis();

        List<RecentBillResponse> recentBills =
                buildRecentBills();

        List<DailySalesResponse> dailySales =
                buildDailySales();

        return new DashboardResponse(
                summary,
                monthlySales,
                dailySales,
                topSellingItems,
                paymentAnalysis,
                orderAnalysis,
                recentBills
        );
    }


    // ============================================================
    // SUMMARY
    // ============================================================

    private DashboardSummaryResponse buildSummary() {

        List<Object[]> rows =
                dashboardRepository.getDashboardSummary();

        DashboardSummaryResponse response =
                new DashboardSummaryResponse();

        if (rows == null || rows.isEmpty()) {
            response.setTodaySales(BigDecimal.ZERO);
            response.setMonthSales(BigDecimal.ZERO);
            response.setTotalSales(BigDecimal.ZERO);
            response.setTotalPaid(BigDecimal.ZERO);
            response.setTotalRemaining(BigDecimal.ZERO);
            response.setBillCount(0L);
            response.setAverageBillValue(BigDecimal.ZERO);

            return response;
        }

        Object[] row = rows.get(0);

        response.setTodaySales(
                toBigDecimal(row[0])
        );

        response.setMonthSales(
                toBigDecimal(row[1])
        );

        response.setTotalSales(
                toBigDecimal(row[2])
        );

        response.setTotalPaid(
                toBigDecimal(row[3])
        );

        response.setTotalRemaining(
                toBigDecimal(row[4])
        );

        response.setBillCount(
                toLong(row[5])
        );

        response.setAverageBillValue(
                toBigDecimal(row[6])
        );

        return response;
    }


    // ============================================================
    // MONTHLY SALES
    // ============================================================

    private List<MonthlySalesResponse> buildMonthlySales() {

        List<Object[]> rows =
                dashboardRepository.getMonthlySales();

        List<MonthlySalesResponse> responses =
                new ArrayList<>();

        for (Object[] row : rows) {

            MonthlySalesResponse response =
                    new MonthlySalesResponse();

            response.setMonth(
                    String.valueOf(row[0])
            );

            response.setYear(
                    toInteger(row[1])
            );

            response.setSalesAmount(
                    toBigDecimal(row[2])
            );

            response.setPaidAmount(
                    toBigDecimal(row[3])
            );

            response.setRemainingAmount(
                    toBigDecimal(row[4])
            );

            response.setBillCount(
                    toLong(row[5])
            );

            responses.add(response);
        }

        return responses;
    }


    // ============================================================
    // TOP SELLING ITEMS
    // ============================================================

    private List<TopSellingItemResponse> buildTopSellingItems() {

        List<Object[]> rows =
                dashboardRepository.getTopSellingItems();

        List<TopSellingItemResponse> responses =
                new ArrayList<>();

        for (Object[] row : rows) {

            TopSellingItemResponse response =
                    new TopSellingItemResponse();

            response.setItemName(
                    String.valueOf(row[0])
            );

            response.setUnit(
                    String.valueOf(row[1])
            );

            response.setQuantitySold(
                    toBigDecimal(row[2])
            );

            response.setSalesAmount(
                    toBigDecimal(row[3])
            );

            response.setNumberOfBills(
                    toLong(row[4])
            );

            responses.add(response);
        }

        return responses;
    }


    // ============================================================
    // PAYMENT ANALYSIS
    // ============================================================

    private PaymentAnalysisResponse buildPaymentAnalysis() {

        List<Object[]> rows =
                dashboardRepository.getPaymentAnalysis();

        PaymentAnalysisResponse response =
                new PaymentAnalysisResponse();

        if (rows == null || rows.isEmpty()) {
            response.setPaidBillCount(0L);
            response.setPartialBillCount(0L);
            response.setUnpaidBillCount(0L);
            response.setPaidAmount(BigDecimal.ZERO);
            response.setRemainingAmount(BigDecimal.ZERO);

            return response;
        }

        Object[] row = rows.get(0);

        response.setPaidBillCount(
                toLong(row[0])
        );

        response.setPartialBillCount(
                toLong(row[1])
        );

        response.setUnpaidBillCount(
                toLong(row[2])
        );

        response.setPaidAmount(
                toBigDecimal(row[3])
        );

        response.setRemainingAmount(
                toBigDecimal(row[4])
        );

        return response;
    }


    // ============================================================
    // ORDER / BILL ANALYSIS
    // ============================================================

    private OrderAnalysisResponse buildOrderAnalysis() {

        List<Object[]> rows =
                dashboardRepository.getOrderAnalysis();

        Long customerOrders = 0L;
        Long manualBills = 0L;
        Long totalBills = 0L;

        if (rows != null && !rows.isEmpty()) {

            Object[] row = rows.get(0);

            customerOrders = toLong(row[0]);
            manualBills = toLong(row[1]);
            totalBills = toLong(row[2]);
        }

        Map<String, Long> statusCounts =
                new LinkedHashMap<>();

        List<Object[]> statusRows =
                dashboardRepository.getOrderStatusCounts();

        for (Object[] statusRow : statusRows) {

            String status =
                    String.valueOf(statusRow[0]);

            Long count =
                    toLong(statusRow[1]);

            statusCounts.put(
                    status,
                    count
            );
        }

        return new OrderAnalysisResponse(
                customerOrders,
                manualBills,
                totalBills,
                statusCounts
        );
    }


    // ============================================================
    // RECENT BILLS
    // ============================================================

    private List<RecentBillResponse> buildRecentBills() {

        List<Object[]> rows =
                dashboardRepository.getRecentBills();

        List<RecentBillResponse> responses =
                new ArrayList<>();

        for (Object[] row : rows) {

            RecentBillResponse response =
                    new RecentBillResponse();

            response.setId(
                    toLong(row[0])
            );

            response.setBillNumber(
                    String.valueOf(row[1])
            );

            response.setBillType(
                    String.valueOf(row[2])
            );

            response.setCustomerName(
                    row[3] != null
                            ? String.valueOf(row[3])
                            : null
            );

            response.setTotalAmount(
                    toBigDecimal(row[4])
            );

            response.setPaidAmount(
                    toBigDecimal(row[5])
            );

            response.setRemainingAmount(
                    toBigDecimal(row[6])
            );

            response.setPaymentStatus(
                    row[7] != null
                            ? String.valueOf(row[7])
                            : null
            );

            response.setBilledAt(
                    toLocalDateTime(row[8])
            );

            responses.add(response);
        }

        return responses;
    }


    // ============================================================
    // CONVERSION HELPERS
    // ============================================================

    private BigDecimal toBigDecimal(Object value) {

        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof Number) {
            return BigDecimal.valueOf(
                    ((Number) value).doubleValue()
            );
        }

        String text = value.toString().trim();

        if (text.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(text);
    }


    private Long toLong(Object value) {

        if (value == null) {
            return 0L;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return Long.parseLong(
                value.toString()
        );
    }


    private Integer toInteger(Object value) {

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        return Integer.parseInt(
                value.toString()
        );
    }


    private LocalDateTime toLocalDateTime(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }

        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }

        return LocalDateTime.parse(
                value.toString()
        );
    }

    private LocalDate toLocalDate(Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }

        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate();
        }

        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalDate();
        }

        return LocalDate.parse(value.toString());
    }

    /*
     * ---------------------------------------------------------
     * DAILY SALES - LAST 7 DAYS
     * ---------------------------------------------------------
     */
    private List<DailySalesResponse> buildDailySales() {

        List<Object[]> rows =
                dashboardRepository.getDailySalesLast7Days();

        List<DailySalesResponse> result =
                new ArrayList<>();

        for (Object[] row : rows) {

            if (row == null || row.length < 5) {
                continue;
            }

            result.add(
                    new DailySalesResponse(
                            toLocalDate(row[0]),
                            toBigDecimal(row[1]),
                            toBigDecimal(row[2]),
                            toBigDecimal(row[3]),
                            toLong(row[4])
                    )
            );
        }

        return result;
    }
}