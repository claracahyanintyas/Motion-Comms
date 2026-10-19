package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentOutput;

@Data @Builder
public class PayPaymentResponse implements PayPaymentOutput {
    Long id;
}
