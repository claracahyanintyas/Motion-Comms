package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data @Builder
public class CommissionOrderPublicDataImpl implements CommissionOrderPublicData {
    private Long id;
    private CommissionPublicData commission;
    private UserPublicData buyer;
    private UserPublicData seller;
    private String status;
    @Builder.Default
    private boolean confirmedBySeller = false;
    @Builder.Default
    private boolean confirmedByBuyer =  false;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @Builder.Default
    private boolean downPaymentPaid =  false;
    @Builder.Default
    private boolean fullPaymentPaid =  false;
    private BigDecimal totalPrice;
    private BigDecimal downPaymentAmount;
    private BigDecimal remainingPaymentAmount;
}
