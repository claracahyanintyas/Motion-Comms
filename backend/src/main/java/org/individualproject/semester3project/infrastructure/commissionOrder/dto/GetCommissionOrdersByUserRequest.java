package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserInput;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommissionOrdersByUserRequest implements GetCommissionOrdersByUserInput {
    private Long userId;
    private String role;
}
