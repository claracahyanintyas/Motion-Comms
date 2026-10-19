package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserOutput;

import java.util.List;

@Builder @Data
public class GetCommissionOrdersByUserResponse implements GetCommissionOrdersByUserOutput {
    List<CommissionOrderPublicData> commissionOrders;
}
