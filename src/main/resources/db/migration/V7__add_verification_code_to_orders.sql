ALTER TABLE orders
    ADD COLUMN verification_code VARCHAR(30);

ALTER TABLE orders
    ADD CONSTRAINT uk_orders_verification_code
    UNIQUE (verification_code);