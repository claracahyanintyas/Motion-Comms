package org.individualproject.semester3project.infrastructure.commissionOrder.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.commissionOrders.dto.ConfirmOrderInput;

@Data @Builder
public class ConfirmOrderRequest implements ConfirmOrderInput {
    Long userId;
}
