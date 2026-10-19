package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commission.dto.UpdateCommissionInput;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class UpdateCommissionRequest implements UpdateCommissionInput {
    String title;
    String description;
    BigDecimal price;
    BigDecimal downPaymentPercentage;
}
