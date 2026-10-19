ALTER TABLE transactions ADD COLUMN status VARCHAR(20);
UPDATE transactions SET status = 'PENDING';
ALTER TABLE transactions ALTER COLUMN status SET NOT NULL;
ALTER TABLE transactions DROP COLUMN confirmed_received;
