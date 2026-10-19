package org.individualproject.semester3project.usecase.commissionOrders.dto;

import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface CommissionOrderPublicData {
    Long getId();
    UserPublicData getBuyer();
    UserPublicData getSeller();
    CommissionPublicData getCommission();
    String getStatus();
    boolean isConfirmedBySeller();
    boolean isConfirmedByBuyer();
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
    boolean isDownPaymentPaid();
    boolean isFullPaymentPaid();
    BigDecimal getTotalPrice();
    BigDecimal getDownPaymentAmount();
    BigDecimal getRemainingPaymentAmount();
}
