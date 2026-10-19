CREATE TABLE payout_methods (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGSERIAL NOT NULL,

    type VARCHAR(50) NOT NULL,
    provider_name VARCHAR(255) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,

    CONSTRAINT fk_payout_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_payout_methods_user_id ON payout_methods(user_id);
CREATE INDEX idx_payout_methods_active ON payout_methods(active);