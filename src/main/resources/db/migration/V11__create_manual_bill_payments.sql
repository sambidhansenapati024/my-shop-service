CREATE TABLE manual_bill_payments (
    id BIGSERIAL PRIMARY KEY,

    bill_id BIGINT NOT NULL,

    amount NUMERIC(12, 2) NOT NULL,

    payment_method VARCHAR(30) NOT NULL,

    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_manual_bill_payments_bill
        FOREIGN KEY (bill_id)
        REFERENCES manual_bills(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_manual_bill_payment_amount
        CHECK (amount > 0)
);

CREATE INDEX idx_manual_bill_payments_bill_id
    ON manual_bill_payments(bill_id);

CREATE INDEX idx_manual_bill_payments_paid_at
    ON manual_bill_payments(paid_at);