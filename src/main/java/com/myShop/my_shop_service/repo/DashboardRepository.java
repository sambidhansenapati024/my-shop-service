package com.myShop.my_shop_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<com.myShop.my_shop_service.entity.Order, Long> {

    /*
     * ============================================================
     * SUMMARY
     * ============================================================
     *
     * Returns:
     *
     * 0 -> todaySales
     * 1 -> monthSales
     * 2 -> totalSales
     * 3 -> totalPaid
     * 4 -> totalRemaining
     * 5 -> billCount
     * 6 -> averageBillValue
     */
    @Query(value = """
        SELECT
            COALESCE(SUM(b.total_amount)
                FILTER (
                    WHERE b.billed_at >= CURRENT_DATE
                    AND b.billed_at < CURRENT_DATE + INTERVAL '1 day'
                ), 0),

            COALESCE(SUM(b.total_amount)
                FILTER (
                    WHERE b.billed_at >= DATE_TRUNC('month', CURRENT_DATE)
                ), 0),

            COALESCE(SUM(b.total_amount), 0),

            COALESCE(SUM(b.paid_amount), 0),

            COALESCE(SUM(b.remaining_amount), 0),

            COUNT(*),

            COALESCE(AVG(b.total_amount), 0)

        FROM (
            SELECT
                total_amount,
                paid_amount,
                remaining_amount,
                billed_at
            FROM orders
            WHERE billed_at IS NOT NULL

            UNION ALL

            SELECT
                total_amount,
                paid_amount,
                remaining_amount,
                billed_at
            FROM manual_bills
            WHERE billed_at IS NOT NULL
        ) b
        """, nativeQuery = true)
    List<Object[]> getDashboardSummary();


    /*
     * ============================================================
     * MONTHLY SALES
     * ============================================================
     *
     * Returns the last 12 months.
     *
     * Includes months with zero sales as well.
     *
     * 0 -> month
     * 1 -> year
     * 2 -> salesAmount
     * 3 -> paidAmount
     * 4 -> remainingAmount
     * 5 -> billCount
     */
    @Query(value = """
        WITH months AS (
            SELECT generate_series(
                DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '11 months',
                DATE_TRUNC('month', CURRENT_DATE),
                INTERVAL '1 month'
            ) AS month_start
        ),

        bills AS (
            SELECT
                total_amount,
                paid_amount,
                remaining_amount,
                billed_at
            FROM orders
            WHERE billed_at IS NOT NULL

            UNION ALL

            SELECT
                total_amount,
                paid_amount,
                remaining_amount,
                billed_at
            FROM manual_bills
            WHERE billed_at IS NOT NULL
        )

        SELECT
            TO_CHAR(m.month_start, 'Mon'),
            EXTRACT(YEAR FROM m.month_start)::INTEGER,

            COALESCE(SUM(b.total_amount), 0),
            COALESCE(SUM(b.paid_amount), 0),
            COALESCE(SUM(b.remaining_amount), 0),

            COUNT(b.billed_at)

        FROM months m

        LEFT JOIN bills b
            ON b.billed_at >= m.month_start
            AND b.billed_at < m.month_start + INTERVAL '1 month'

        GROUP BY m.month_start

        ORDER BY m.month_start
        """, nativeQuery = true)
    List<Object[]> getMonthlySales();


    /*
     * ============================================================
     * TOP SELLING ITEMS
     * ============================================================
     *
     * Combines:
     *   orders -> order_items
     *   manual_bills -> manual_bill_items
     *
     * Groups by item name + unit.
     *
     * This is important because:
     *
     *   Rice + kg
     *   Rice + gm
     *
     * should not be combined into one quantity.
     *
     * Returns:
     *
     * 0 -> itemName
     * 1 -> unit
     * 2 -> quantitySold
     * 3 -> salesAmount
     * 4 -> numberOfBills
     */
    @Query(value = """
        WITH all_items AS (

            SELECT
                oi.item_name,
                oi.unit,
                oi.quantity,
                oi.item_total,
                CONCAT('ORDER-', o.id) AS bill_key

            FROM order_items oi

            INNER JOIN orders o
                ON o.id = oi.order_id

            WHERE o.billed_at IS NOT NULL
              AND oi.item_total IS NOT NULL


            UNION ALL


            SELECT
                mbi.item_name,
                mbi.unit,
                mbi.quantity,
                mbi.total_price,
                CONCAT('MANUAL-', mb.id) AS bill_key

            FROM manual_bill_items mbi

            INNER JOIN manual_bills mb
                ON mb.id = mbi.bill_id

            WHERE mb.billed_at IS NOT NULL
              AND mbi.total_price IS NOT NULL
        )

        SELECT
            MIN(item_name) AS item_name,
            MIN(unit) AS unit,

            COALESCE(SUM(quantity), 0) AS quantity_sold,

            COALESCE(SUM(item_total), 0) AS sales_amount,

            COUNT(DISTINCT bill_key) AS number_of_bills

        FROM all_items

        GROUP BY
            LOWER(TRIM(item_name)),
            LOWER(TRIM(unit))

        ORDER BY
            sales_amount DESC

        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> getTopSellingItems();


    /*
     * ============================================================
     * PAYMENT ANALYSIS
     * ============================================================
     *
     * Returns:
     *
     * 0 -> paidBillCount
     * 1 -> partialBillCount
     * 2 -> unpaidBillCount
     * 3 -> paidAmount
     * 4 -> remainingAmount
     */
    @Query(value = """
        SELECT

            COUNT(*) FILTER (
                WHERE UPPER(payment_status) = 'PAID'
            ),

            COUNT(*) FILTER (
                WHERE UPPER(payment_status) = 'PARTIAL'
            ),

            COUNT(*) FILTER (
                WHERE UPPER(payment_status) = 'UNPAID'
            ),

            COALESCE(SUM(paid_amount), 0),

            COALESCE(SUM(remaining_amount), 0)

        FROM (

            SELECT
                payment_status,
                paid_amount,
                remaining_amount
            FROM orders
            WHERE billed_at IS NOT NULL

            UNION ALL

            SELECT
                payment_status,
                paid_amount,
                remaining_amount
            FROM manual_bills
            WHERE billed_at IS NOT NULL

        ) b
        """, nativeQuery = true)
    List<Object[]> getPaymentAnalysis();


    /*
     * ============================================================
     * ORDER / BILL ANALYSIS
     * ============================================================
     *
     * Returns:
     *
     * 0 -> customerOrders
     * 1 -> manualBills
     * 2 -> totalBills
     */
    @Query(value = """
        SELECT

            (
                SELECT COUNT(*)
                FROM orders
                WHERE billed_at IS NOT NULL
            ),

            (
                SELECT COUNT(*)
                FROM manual_bills
                WHERE billed_at IS NOT NULL
            ),

            (
                SELECT COUNT(*)
                FROM (
                    SELECT id
                    FROM orders
                    WHERE billed_at IS NOT NULL

                    UNION ALL

                    SELECT id
                    FROM manual_bills
                    WHERE billed_at IS NOT NULL
                ) all_bills
            )
        """, nativeQuery = true)
    List<Object[]> getOrderAnalysis();


    /*
     * ============================================================
     * CUSTOMER ORDER STATUS COUNTS
     * ============================================================
     *
     * Used later by the service to build:
     *
     * {
     *     "RECEIVED": 2,
     *     "PACKING": 3,
     *     "READY": 4,
     *     "BILLED": 10
     * }
     */
    @Query(value = """
        SELECT
            status,
            COUNT(*)

        FROM orders

        WHERE billed_at IS NOT NULL

        GROUP BY status

        ORDER BY COUNT(*) DESC
        """, nativeQuery = true)
    List<Object[]> getOrderStatusCounts();


    /*
     * ============================================================
     * RECENT BILLS
     * ============================================================
     *
     * Returns the latest 10 bills from both sources.
     *
     * 0 -> id
     * 1 -> billNumber
     * 2 -> billType
     * 3 -> customerName
     * 4 -> totalAmount
     * 5 -> paidAmount
     * 6 -> remainingAmount
     * 7 -> paymentStatus
     * 8 -> billedAt
     */
    @Query(value = """
        SELECT *
        FROM (

            SELECT
                o.id,
                o.order_number AS bill_number,
                'ORDER' AS bill_type,

                u.name AS customer_name,

                o.total_amount,
                o.paid_amount,
                o.remaining_amount,
                o.payment_status,
                o.billed_at

            FROM orders o

            INNER JOIN users u
                ON u.id = o.user_id

            WHERE o.billed_at IS NOT NULL


            UNION ALL


            SELECT
                mb.id,
                mb.bill_number,
                'MANUAL' AS bill_type,

                mb.customer_name,

                mb.total_amount,
                mb.paid_amount,
                mb.remaining_amount,
                mb.payment_status,
                mb.billed_at

            FROM manual_bills mb

            WHERE mb.billed_at IS NOT NULL

        ) bills

        ORDER BY billed_at DESC

        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> getRecentBills();


    /*
     * ---------------------------------------------------------
     * DAILY SALES - LAST 7 DAYS
     * ---------------------------------------------------------
     *
     * Includes:
     * - Customer order bills
     * - Manual bills
     *
     * Returns every day, including days with zero sales.
     */
    @Query(value = """
        WITH dates AS (
            SELECT
                generate_series(
                    CURRENT_DATE - INTERVAL '6 days',
                    CURRENT_DATE,
                    INTERVAL '1 day'
                )::date AS sale_date
        ),

        all_bills AS (

            SELECT
                billed_at::date AS sale_date,
                total_amount,
                paid_amount,
                remaining_amount

            FROM orders

            WHERE billed_at IS NOT NULL


            UNION ALL


            SELECT
                billed_at::date AS sale_date,
                total_amount,
                paid_amount,
                remaining_amount

            FROM manual_bills

            WHERE billed_at IS NOT NULL
        )

        SELECT
            d.sale_date,

            COALESCE(
                SUM(b.total_amount),
                0
            ) AS sales_amount,

            COALESCE(
                SUM(b.paid_amount),
                0
            ) AS paid_amount,

            COALESCE(
                SUM(b.remaining_amount),
                0
            ) AS remaining_amount,

            COUNT(b.sale_date) AS bill_count

        FROM dates d

        LEFT JOIN all_bills b
            ON b.sale_date = d.sale_date

        GROUP BY d.sale_date

        ORDER BY d.sale_date ASC
        """, nativeQuery = true)
    List<Object[]> getDailySalesLast7Days();

}