package com.myShop.my_shop_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.myShop.my_shop_service.entity.Product;

import java.util.List;

@Repository
public interface InventoryDashboardRepository extends JpaRepository<Product, Long> {

    /*
     * ---------------------------------------------------------
     * INVENTORY SUMMARY
     * ---------------------------------------------------------
     *
     * Returns:
     * 0 -> total products
     * 1 -> total batches
     * 2 -> low stock products
     * 3 -> out of stock products
     * 4 -> expiring soon batches
     * 5 -> expired batches
     */
    @Query(value = """
            SELECT
                (SELECT COUNT(*)
                 FROM products) AS total_products,

                (SELECT COUNT(*)
                 FROM product_batches) AS total_batches,

                (
                    SELECT COUNT(*)
                    FROM (
                        SELECT p.id
                        FROM products p
                        LEFT JOIN product_batches pb
                            ON pb.product_id = p.id
                        GROUP BY p.id
                        HAVING COALESCE(SUM(pb.quantity), 0) > 0
                           AND COALESCE(SUM(pb.quantity), 0) <= 5
                    ) low_stock
                ) AS low_stock_products,

                (
                    SELECT COUNT(*)
                    FROM (
                        SELECT p.id
                        FROM products p
                        LEFT JOIN product_batches pb
                            ON pb.product_id = p.id
                        GROUP BY p.id
                        HAVING COALESCE(SUM(pb.quantity), 0) = 0
                    ) out_of_stock
                ) AS out_of_stock_products,

                (
                    SELECT COUNT(*)
                    FROM product_batches pb
                    WHERE pb.quantity > 0
                      AND pb.expiry_date IS NOT NULL
                      AND pb.expiry_date >= CURRENT_DATE
                      AND pb.expiry_date <= CURRENT_DATE + INTERVAL '30 days'
                ) AS expiring_soon_batches,

                (
                    SELECT COUNT(*)
                    FROM product_batches pb
                    WHERE pb.quantity > 0
                      AND pb.expiry_date IS NOT NULL
                      AND pb.expiry_date < CURRENT_DATE
                ) AS expired_batches
            """, nativeQuery = true)
    List<Object[]> getInventorySummary();


    /*
     * ---------------------------------------------------------
     * LOW STOCK PRODUCTS
     * ---------------------------------------------------------
     *
     * A product is considered low stock when:
     *
     * quantity > 0
     * quantity <= 5
     *
     * Product unit is taken from the products table.
     */
    @Query(value = """
            SELECT
                p.id AS product_id,
                p.product_name,
                p.barcode,
                p.unit,
                COALESCE(SUM(pb.quantity), 0) AS available_quantity

            FROM products p

            LEFT JOIN product_batches pb
                ON pb.product_id = p.id

            GROUP BY
                p.id,
                p.product_name,
                p.barcode,
                p.unit

            HAVING COALESCE(SUM(pb.quantity), 0) > 0
               AND COALESCE(SUM(pb.quantity), 0) <= 5

            ORDER BY
                available_quantity ASC,
                p.product_name ASC

            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> getLowStockProducts();


    /*
     * ---------------------------------------------------------
     * EXPIRING SOON BATCHES
     * ---------------------------------------------------------
     *
     * Shows batches that:
     *
     * - have stock
     * - have an expiry date
     * - expire today or within the next 30 days
     */
    @Query(value = """
            SELECT
                pb.id AS batch_id,
                p.id AS product_id,
                p.product_name,
                pb.batch_number,
                pb.quantity,
                p.unit,
                pb.expiry_date,
                (pb.expiry_date - CURRENT_DATE) AS days_remaining

            FROM product_batches pb

            INNER JOIN products p
                ON p.id = pb.product_id

            WHERE pb.quantity > 0
              AND pb.expiry_date IS NOT NULL
              AND pb.expiry_date >= CURRENT_DATE
              AND pb.expiry_date <= CURRENT_DATE + INTERVAL '30 days'

            ORDER BY
                pb.expiry_date ASC

            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> getExpiringBatches();


    /*
     * ---------------------------------------------------------
     * EXPIRED BATCHES
     * ---------------------------------------------------------
     *
     * Shows batches that:
     *
     * - still have stock
     * - have already expired
     */
    @Query(value = """
            SELECT
                pb.id AS batch_id,
                p.id AS product_id,
                p.product_name,
                pb.batch_number,
                pb.quantity,
                p.unit,
                pb.expiry_date,
                (pb.expiry_date - CURRENT_DATE) AS days_remaining

            FROM product_batches pb

            INNER JOIN products p
                ON p.id = pb.product_id

            WHERE pb.quantity > 0
              AND pb.expiry_date IS NOT NULL
              AND pb.expiry_date < CURRENT_DATE

            ORDER BY
                pb.expiry_date DESC

            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> getExpiredBatches();

    /*
     * ---------------------------------------------------------
     * INVENTORY VALUE
     * ---------------------------------------------------------
     *
     * Calculates the value of currently available stock.
     *
     * Purchase Value = quantity × purchase price
     * Selling Value  = quantity × selling price
     *
     * Only stock with quantity > 0 is included.
     */
    @Query(value = """
        SELECT
            COALESCE(
                SUM(pb.quantity * pb.purchase_price),
                0
            ) AS purchase_value,

            COALESCE(
                SUM(pb.quantity * pb.selling_price),
                0
            ) AS selling_value

        FROM product_batches pb

        WHERE pb.quantity > 0
        """, nativeQuery = true)
    List<Object[]> getInventoryValue();

}