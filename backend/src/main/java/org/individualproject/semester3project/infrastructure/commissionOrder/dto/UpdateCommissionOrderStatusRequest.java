package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.usecase.commissionOrders.dto.UpdateCommissionOrderStatusInput;

@Data
@AllArgsConstructor
@NoArgsConstructor @Builder
public class UpdateCommissionOrderStatusRequest implements UpdateCommissionOrderStatusInput {
    CommissionOrderStatus commissionOrderStatus;
}
