-- Add payment fields to orders table

ALTER TABLE orders
ADD COLUMN payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID';

ALTER TABLE orders
ADD COLUMN paid_amount NUMERIC(12, 2) NOT NULL DEFAULT 0.00;

ALTER TABLE orders
ADD COLUMN remaining_amount NUMERIC(12, 2) NOT NULL DEFAULT 0.00;

-- Set remaining amount for existing billed orders
UPDATE orders
SET remaining_amount = COALESCE(total_amount, 0.00)
WHERE total_amount IS NOT NULL;