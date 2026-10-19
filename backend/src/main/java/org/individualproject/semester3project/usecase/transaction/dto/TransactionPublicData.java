package org.individualproject.semester3project.usecase.transaction.dto;

import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface TransactionPublicData {
    Long getId();
    CommissionOrderPublicData getCommissionOrder();
    TransactionType getType();
    OffsetDateTime getCreatedAt();
    TransactionStatus getStatus();
    OffsetDateTime getConfirmedAt();
    String getProofUrl();
    BigDecimal getAmount();
}
