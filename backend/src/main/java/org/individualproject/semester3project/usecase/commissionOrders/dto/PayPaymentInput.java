package org.individualproject.semester3project.usecase.commissionOrders.dto;

import org.individualproject.semester3project.domain.transaction.model.TransactionType;

import java.math.BigDecimal;

public interface PayPaymentInput {
    Long getOrderId();
    Long getPayoutMethodId();
    TransactionType getType();
    BigDecimal getAmount();
    String getProofUrl();
    Long getUserId();
}
