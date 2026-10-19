package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderOutput;

@Data
@Builder
public class CreateCommissionOrderResponse implements CreateCommissionOrderOutput {
    private Long id;
}
