-- Add billing fields to order_items

ALTER TABLE order_items
ADD COLUMN unit_price NUMERIC(10, 2);

ALTER TABLE order_items
ADD COLUMN item_total NUMERIC(10, 2);


-- Add billing fields to orders

ALTER TABLE orders
ADD COLUMN total_amount NUMERIC(12, 2);

ALTER TABLE orders
ADD COLUMN billed_at TIMESTAMP;