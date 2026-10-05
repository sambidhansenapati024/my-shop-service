ALTER TABLE manual_bills
ADD COLUMN billed_at TIMESTAMP NULL;

CREATE INDEX idx_manual_bills_billed_at
ON manual_bills(billed_at);