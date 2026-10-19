package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderInput;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommissionOrderRequest implements CreateCommissionOrderInput {
    private Long commissionId;
    private Long buyerId;

    public CreateCommissionOrderRequest(Long commissionId)
    {
        this.commissionId = commissionId;
    }
}
