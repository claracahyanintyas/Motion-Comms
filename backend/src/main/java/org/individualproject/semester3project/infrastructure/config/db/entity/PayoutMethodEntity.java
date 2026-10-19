package org.individualproject.semester3project.infrastructure.config.db.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payout_methods")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayoutMethodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PayoutMethodType type;

    @NotNull
    @Column(name = "provider_name")
    private String providerName;

    @NotNull
    @Column(name = "account_name")
    private String accountName;

    @NotNull
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @Column(name = "active")
    private boolean active;

    @NotNull
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public static PayoutMethodEntity toEntity(PayoutMethod method) {
        return PayoutMethodEntity.builder()
                .id(method.getId())
                .user(UserEntity.toEntity(method.getUser()))
                .type(method.getType())
                .providerName(method.getProviderName())
                .accountName(method.getAccountName())
                .accountNumber(method.getAccountNumber())
                .active(method.isActive())
                .createdAt(method.getCreatedAt())
                .deletedAt(method.getDeletedAt())
                .build();
    }

    public PayoutMethod fromEntity() {
        return PayoutMethod.builder()
                .id(this.id)
                .user(this.user.fromEntity())
                .type(this.type)
                .providerName(this.providerName)
                .accountName(this.accountName)
                .accountNumber(this.accountNumber)
                .active(this.active)
                .createdAt(this.createdAt)
                .deletedAt(this.deletedAt)
                .build();
    }
}
