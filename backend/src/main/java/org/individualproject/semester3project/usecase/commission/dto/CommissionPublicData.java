package org.individualproject.semester3project.usecase.commission.dto;

import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.math.BigDecimal;

public interface CommissionPublicData {
    Long getId();
    String getTitle();
    String getDescription();
    BigDecimal getPrice();
    UserPublicData getCommissionee();
    BigDecimal getDownPaymentPercentage();
}
