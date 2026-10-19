package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionOutput;

@Data @Builder @AllArgsConstructor
@NoArgsConstructor
public class GetCommissionResponse implements GetCommissionOutput {
    private CommissionPublicData commission;
}
