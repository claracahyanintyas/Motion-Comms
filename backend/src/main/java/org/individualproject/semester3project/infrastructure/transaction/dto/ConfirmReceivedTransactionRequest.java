package org.individualproject.semester3project.infrastructure.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.transaction.dto.ConfirmReceivedTransactionInput;

@Data @Builder
public class ConfirmReceivedTransactionRequest implements ConfirmReceivedTransactionInput {
    Long orderId;
}
