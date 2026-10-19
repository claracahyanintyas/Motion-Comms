package org.individualproject.semester3project.usecase.transaction;

import org.individualproject.semester3project.usecase.transaction.dto.ConfirmReceivedTransactionInput;

public interface ConfirmReceivedTransactionUseCase {
    void confirm(ConfirmReceivedTransactionInput input);
}
