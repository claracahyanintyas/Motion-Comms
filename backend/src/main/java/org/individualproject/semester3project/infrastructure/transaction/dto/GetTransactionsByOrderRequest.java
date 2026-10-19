package org.individualproject.semester3project.infrastructure.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderInput;

@Data @Builder
public class GetTransactionsByOrderRequest implements GetTransactionsByOrderInput {
    Long orderId;
}
