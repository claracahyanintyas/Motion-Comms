package org.individualproject.semester3project.infrastructure.config.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.individualproject.semester3project.domain.commission.model.Commission;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "commissions")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 1 ,max = 150)
    @Column(name = "title")
    private String title;

    @Length(max = 1000)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "commissionee_id")
    private UserEntity commissionee;

    @NotNull
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @NotNull
    @Column(name = "down_payment_percentage")
    private BigDecimal downPaymentPercentage;

    public static CommissionEntity toEntity(Commission commission) {
        return CommissionEntity.builder()
                .id(commission.getId())
                .title(commission.getTitle())
                .description(commission.getDescription())
                .price(commission.getPrice())
                .commissionee(UserEntity.toEntity(commission.getCommissionee()))
                .createdAt(commission.getCreatedAt())
                .updatedAt(commission.getUpdatedAt())
                .downPaymentPercentage(commission.getDownPaymentPercentage())
                .build();
    }

    public Commission fromEntity() {
        return Commission.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .commissionee(commissionee.fromEntity())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .downPaymentPercentage(downPaymentPercentage)
                .build();
    }
}