package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.commission.dto.GetAllCommissionsOutput;

import java.util.List;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class GetAllCommissionsResponse implements GetAllCommissionsOutput {
    List<CommissionPublicData> commissions;
}
