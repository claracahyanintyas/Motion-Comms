package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.UpdateCommissionOrderStatusInput;

public interface UpdateCommissionOrderStatusUseCase {
    void updateCommissionOrderStatus(Long orderId,UpdateCommissionOrderStatusInput input);
}
