CREATE TABLE manual_bills (
    id BIGSERIAL PRIMARY KEY,

    bill_number VARCHAR(50) NOT NULL,

    customer_name VARCHAR(150),

    customer_mobile VARCHAR(20),

    subtotal NUMERIC(12, 2) NOT NULL DEFAULT 0,

    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,

    paid_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,

    remaining_amount NUMERIC(12, 2) NOT NULL DEFAULT 0,

    payment_status VARCHAR(30) NOT NULL DEFAULT 'UNPAID',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_manual_bills_bill_number
        UNIQUE (bill_number),

    CONSTRAINT chk_manual_bills_subtotal
        CHECK (subtotal >= 0),

    CONSTRAINT chk_manual_bills_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT chk_manual_bills_paid_amount
        CHECK (paid_amount >= 0),

    CONSTRAINT chk_manual_bills_remaining_amount
        CHECK (remaining_amount >= 0)
);


CREATE TABLE manual_bill_items (
    id BIGSERIAL PRIMARY KEY,

    bill_id BIGINT NOT NULL,

    product_id BIGINT,

    product_batch_id BIGINT,

    item_name VARCHAR(200) NOT NULL,

    quantity NUMERIC(12, 3) NOT NULL,

    unit VARCHAR(30) NOT NULL,

    unit_price NUMERIC(12, 2) NOT NULL,

    total_price NUMERIC(12, 2) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_manual_bill_items_bill
        FOREIGN KEY (bill_id)
        REFERENCES manual_bills(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_manual_bill_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_manual_bill_items_batch
        FOREIGN KEY (product_batch_id)
        REFERENCES product_batches(id)
        ON DELETE SET NULL,

    CONSTRAINT chk_manual_bill_items_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_manual_bill_items_unit_price
        CHECK (unit_price >= 0),

    CONSTRAINT chk_manual_bill_items_total_price
        CHECK (total_price >= 0)
);


CREATE INDEX idx_manual_bill_items_bill_id
    ON manual_bill_items(bill_id);

CREATE INDEX idx_manual_bill_items_product_id
    ON manual_bill_items(product_id);

CREATE INDEX idx_manual_bill_items_batch_id
    ON manual_bill_items(product_batch_id);

CREATE INDEX idx_manual_bills_created_at
    ON manual_bills(created_at);