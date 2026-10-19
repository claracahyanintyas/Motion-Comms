package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.ConfirmOrderInput;

public interface ConfirmOrderByBuyerUseCase {
    void confirmByBuyer(Long orderId, ConfirmOrderInput input);
}
