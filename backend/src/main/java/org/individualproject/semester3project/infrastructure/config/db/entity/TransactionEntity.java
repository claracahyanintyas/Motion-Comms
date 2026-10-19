package org.individualproject.semester3project.infrastructure.config.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.individualproject.semester3project.domain.transaction.model.Transaction;
import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "commission_order_id")
    private CommissionOrderEntity commissionOrder;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payout_method_id")
    private PayoutMethodEntity payoutMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "proof_url")
    private String proofUrl;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status;

    @Column(name = "confirmed_at")
    private OffsetDateTime confirmedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;


    public static TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .commissionOrder(
                        CommissionOrderEntity.toEntity(transaction.getCommissionOrder())
                )
                .payoutMethod(
                        PayoutMethodEntity.toEntity(transaction.getPayoutMethod())
                )
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .proofUrl(transaction.getProofUrl())
                .status(transaction.getStatus())
                .confirmedAt(transaction.getConfirmedAt())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public Transaction fromEntity() {
        return Transaction.builder()
                .id(this.id)
                .commissionOrder(this.commissionOrder.fromEntity())
                .payoutMethod(this.payoutMethod.fromEntity())
                .type(this.type)
                .amount(this.amount)
                .proofUrl(this.proofUrl)
                .status(this.status)
                .confirmedAt(this.confirmedAt)
                .createdAt(this.createdAt)
                .build();
    }
}
