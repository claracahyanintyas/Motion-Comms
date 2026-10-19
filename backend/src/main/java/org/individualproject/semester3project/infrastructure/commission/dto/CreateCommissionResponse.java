package org.individualproject.semester3project.infrastructure.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionOutput;
@Data
@AllArgsConstructor
@Builder

public class CreateCommissionResponse implements CreateCommissionOutput {
    private Long id;
}
