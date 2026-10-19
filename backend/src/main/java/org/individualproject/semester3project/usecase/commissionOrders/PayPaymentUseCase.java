package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentOutput;

public interface PayPaymentUseCase {
    PayPaymentOutput pay(PayPaymentInput input);
}
