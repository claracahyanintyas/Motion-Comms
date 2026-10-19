package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentInput;

import java.math.BigDecimal;

@Data @Builder
public class PayPaymentRequest implements PayPaymentInput {
    Long orderId;
    Long payoutMethodId;
    TransactionType type;
    BigDecimal amount;
    String proofUrl;
    Long userId;
}
