package org.individualproject.semester3project.usecase.commission.dto;

import java.math.BigDecimal;

public interface UpdateCommissionInput {
    String getTitle();
    String getDescription();
    BigDecimal getPrice();
    BigDecimal getDownPaymentPercentage();
}
