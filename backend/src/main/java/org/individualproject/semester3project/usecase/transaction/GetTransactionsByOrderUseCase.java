package org.individualproject.semester3project.usecase.transaction;

import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderInput;
import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderOutput;

public interface GetTransactionsByOrderUseCase {
    GetTransactionsByOrderOutput getTransactionsByOrder(GetTransactionsByOrderInput input);
}
