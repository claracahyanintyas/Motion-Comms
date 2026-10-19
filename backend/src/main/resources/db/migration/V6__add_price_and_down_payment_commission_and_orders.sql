ALTER TABLE commissions
ADD down_payment_percentage NUMERIC(5,4) NOT NULL default 0.3;

ALTER TABLE commission_orders
ADD down_payment_paid BOOLEAN NOT NULL default false,
ADD full_payment_paid BOOLEAN NOT NULL default false,
ADD total_price NUMERIC(12,2) NOT NULL CHECK (total_price >= 0.01) default 0.01,
ADD down_payment_amount NUMERIC(12,2) NOT NULL CHECK (down_payment_amount >= 0.01) default 0.01,
ADD remaining_payment_amount NUMERIC(12,2) NOT NULL CHECK (remaining_payment_amount >= 0.01) default 0.01;


