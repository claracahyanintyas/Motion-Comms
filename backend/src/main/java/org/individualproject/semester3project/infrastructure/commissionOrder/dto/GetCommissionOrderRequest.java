package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderInput;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class GetCommissionOrderRequest implements GetCommissionOrderInput {
    Long id;
}
