ALTER TABLE commission_orders
    ADD CONSTRAINT pk_commission_orders PRIMARY KEY (id);



CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    commission_order_id BIGSERIAL NOT NULL,
    payout_method_id BIGSERIAL NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    proof_url TEXT NOT NULL,
    confirmed_received BOOLEAN NOT NULL DEFAULT FALSE,
    confirmed_at TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_commission_order
        FOREIGN KEY (commission_order_id)
            REFERENCES commission_orders (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_transactions_payout_method
        FOREIGN KEY (payout_method_id)
            REFERENCES payout_methods (id)
);
