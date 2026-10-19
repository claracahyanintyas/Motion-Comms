package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderOutput;

public interface GetCommissionOrderUseCase {
    GetCommissionOrderOutput getCommissionOrder(GetCommissionOrderInput input);
}
