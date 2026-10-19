package org.individualproject.semester3project.domain.transaction.model;

import lombok.Builder;
import lombok.Getter;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Builder
public class Transaction {
    Long id;
    CommissionOrder commissionOrder;
    PayoutMethod payoutMethod;
    TransactionType type;
    @Builder.Default
    OffsetDateTime createdAt =  OffsetDateTime.now();
    OffsetDateTime confirmedAt;
    @Builder.Default
    TransactionStatus status = TransactionStatus.PENDING;
    String proofUrl;
    private BigDecimal amount;

    public void confirmReceived() {
        if (status == TransactionStatus.CONFIRMED || status == TransactionStatus.REJECTED) {
            throw new IllegalStateException("Already confirmed or rejected");
        }
        this.status = TransactionStatus.CONFIRMED;
        this.confirmedAt = OffsetDateTime.now();
    }
}
