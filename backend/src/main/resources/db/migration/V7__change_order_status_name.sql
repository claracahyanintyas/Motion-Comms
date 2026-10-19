UPDATE commission_orders
SET commission_order_status = 'PENDING_DOWN_PAYMENT'
WHERE commission_order_status = 'PENDING_PAYMENT';