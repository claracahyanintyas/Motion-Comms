package org.individualproject.semester3project.usecase.commission.dto;

import java.math.BigDecimal;

public interface CreateCommissionInput {
    String getTitle();
    String getDescription();
    BigDecimal getPrice();
    Long getCommissioneeId();
    BigDecimal getDownPaymentPercentage();
}
