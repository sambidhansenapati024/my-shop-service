-- ============================================================
-- INVENTORY
-- ============================================================

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,

    product_name VARCHAR(200) NOT NULL,

    barcode VARCHAR(100),

    unit VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- A barcode normally identifies the product.
-- NULL is allowed because some products may not have a barcode.
CREATE UNIQUE INDEX uk_products_barcode
    ON products (barcode)
    WHERE barcode IS NOT NULL;


-- ============================================================
-- PRODUCT BATCHES
-- ============================================================

CREATE TABLE product_batches (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    batch_number VARCHAR(100) NOT NULL,

    purchase_price NUMERIC(12, 2) NOT NULL,

    selling_price NUMERIC(12, 2) NOT NULL,

    quantity NUMERIC(12, 3) NOT NULL DEFAULT 0,

    expiry_date DATE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_batches_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_product_batches_purchase_price
        CHECK (purchase_price >= 0),

    CONSTRAINT chk_product_batches_selling_price
        CHECK (selling_price >= 0),

    CONSTRAINT chk_product_batches_quantity
        CHECK (quantity >= 0)
);


-- The same batch number should not be duplicated
-- for the same product.
CREATE UNIQUE INDEX uk_product_batch_number
    ON product_batches (product_id, batch_number);


-- Useful for finding available/expiring batches.
CREATE INDEX idx_product_batches_product_id
    ON product_batches (product_id);

CREATE INDEX idx_product_batches_expiry_date
    ON product_batches (expiry_date);