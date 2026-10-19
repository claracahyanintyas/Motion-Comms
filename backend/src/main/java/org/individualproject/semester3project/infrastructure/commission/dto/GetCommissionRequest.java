package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionInput;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommissionRequest implements GetCommissionInput {
    private Long id;
}
