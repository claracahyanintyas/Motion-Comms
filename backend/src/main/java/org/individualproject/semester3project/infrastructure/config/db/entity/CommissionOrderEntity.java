package org.individualproject.semester3project.infrastructure.config.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "commission_orders")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "commission_id")
    private CommissionEntity commission;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "commission_order_status", nullable = false)
    private CommissionOrderStatus commissionOrderStatus;

    @NotNull
    @Column(name = "confirmed_by_seller")
    private boolean confirmedBySeller;

    @NotNull
    @Column(name = "confirmed_by_buyer")
    private boolean confirmedByBuyer;

    @NotNull
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @NotNull
    @Column(name = "down_payment_paid")
    boolean downPaymentPaid = false;
    @NotNull
    @Column(name = "full_payment_paid")
    boolean fullPaymentPaid = false;
    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @NotNull
    @Column(name = "down_payment_amount")
    private BigDecimal downPaymentAmount;
    @NotNull
    @Column(name = "remaining_payment_amount")
    private BigDecimal remainingPaymentAmount;

    public static CommissionOrderEntity toEntity(CommissionOrder commissionOrder) {
        return CommissionOrderEntity.builder()
                .id(commissionOrder.getId())
                .commission(CommissionEntity.toEntity(commissionOrder.getCommission()))
                .seller(UserEntity.toEntity(commissionOrder.getSeller()))
                .buyer(UserEntity.toEntity(commissionOrder.getBuyer()))
                .createdAt(commissionOrder.getCreatedAt())
                .updatedAt(commissionOrder.getUpdatedAt())
                .confirmedBySeller(commissionOrder.isConfirmedBySeller())
                .confirmedByBuyer(commissionOrder.isConfirmedByBuyer())
                .commissionOrderStatus(commissionOrder.getCommissionOrderStatus())
                .downPaymentPaid(commissionOrder.isDownPaymentPaid())
                .fullPaymentPaid(commissionOrder.isFullPaymentPaid())
                .totalPrice(commissionOrder.getTotalPrice())
                .downPaymentAmount(commissionOrder.getDownPaymentAmount())
                .remainingPaymentAmount(commissionOrder.getRemainingPaymentAmount())
                .build();
    }

    public CommissionOrder fromEntity() {
        return CommissionOrder.builder()
                .id(id)
                .commission(commission.fromEntity())
                .seller(seller.fromEntity())
                .buyer(buyer.fromEntity())
                .commissionOrderStatus(commissionOrderStatus)
                .confirmedBySeller(confirmedBySeller)
                .confirmedByBuyer(confirmedByBuyer)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .downPaymentPaid(downPaymentPaid)
                .fullPaymentPaid(fullPaymentPaid)
                .totalPrice(totalPrice)
                .downPaymentAmount(downPaymentAmount)
                .remainingPaymentAmount(remainingPaymentAmount)
                .build();
    }
}
