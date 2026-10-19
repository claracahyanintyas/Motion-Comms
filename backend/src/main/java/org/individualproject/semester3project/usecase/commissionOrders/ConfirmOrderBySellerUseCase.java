package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.ConfirmOrderInput;

public interface ConfirmOrderBySellerUseCase {
    void confirmBySeller(Long orderId, ConfirmOrderInput input);
}
