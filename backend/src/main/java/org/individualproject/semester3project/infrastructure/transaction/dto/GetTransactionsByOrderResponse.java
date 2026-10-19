package org.individualproject.semester3project.infrastructure.transaction.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderOutput;
import org.individualproject.semester3project.usecase.transaction.dto.TransactionPublicData;

import java.util.List;

@Data @Builder
public class GetTransactionsByOrderResponse implements GetTransactionsByOrderOutput {
    List<TransactionPublicData> transactions;
}
