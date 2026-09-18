
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,

    order_id BIGINT NOT NULL,

    amount NUMERIC(12, 2) NOT NULL,

    payment_method VARCHAR(20) NOT NULL,

    payment_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);