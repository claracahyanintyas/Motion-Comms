package org.individualproject.semester3project.infrastructure.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;
import org.individualproject.semester3project.usecase.transaction.dto.TransactionPublicData;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data @Builder
public class TransactionPublicDataImpl implements TransactionPublicData {
    Long id;
    CommissionOrderPublicData commissionOrder;
    PayoutMethodPublicData payoutMethod;
    TransactionType type;
    OffsetDateTime createdAt;
    OffsetDateTime confirmedAt;
    TransactionStatus status;
    String proofUrl;
    BigDecimal amount;
}
