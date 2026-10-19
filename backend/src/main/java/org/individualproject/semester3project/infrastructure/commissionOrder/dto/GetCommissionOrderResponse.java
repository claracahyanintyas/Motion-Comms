package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderOutput;

@Data @NoArgsConstructor
@Builder @AllArgsConstructor
public class GetCommissionOrderResponse implements GetCommissionOrderOutput {
    CommissionOrderPublicData commissionOrder;
}
