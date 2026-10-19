CREATE TABLE commission_orders (
    id BIGSERIAL NOT NULL,
    commission_id BIGSERIAL NOT NULL REFERENCES commissions(id),
    seller_id BIGSERIAL NOT NULL REFERENCES users(id),
    buyer_id BIGSERIAL NOT NULL REFERENCES users(id),
    status TEXT NOT NULL,
    confirmed_by_seller BOOLEAN NOT NULL DEFAULT false,
    confirmed_by_buyer BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);