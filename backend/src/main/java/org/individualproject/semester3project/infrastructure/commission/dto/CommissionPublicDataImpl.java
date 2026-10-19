package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class CommissionPublicDataImpl implements CommissionPublicData {
    Long id;
    String title;
    String description;
    BigDecimal price;
    UserPublicData commissionee;
    BigDecimal downPaymentPercentage;
}
