package org.individualproject.semester3project.infrastructure.commission.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionInput;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder @NoArgsConstructor

public class CreateCommissionRequest implements CreateCommissionInput {

    @NotBlank
    @Size(min = 1, message = "{title is too short}")
    @Size(max = 200, message = "{title cannot be more than 200}")
    private String title;

    @Size(max = 1000, message = "{title cannot be more than 200}")
    private String description;

    @DecimalMin(value = "0.01")
    private BigDecimal price;

    private Long commissioneeId;

    private BigDecimal downPaymentPercentage;
}
