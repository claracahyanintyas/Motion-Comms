package org.individualproject.semester3project.usecase.transaction.dto;

import java.util.List;

public interface GetTransactionsByOrderOutput {
    List<TransactionPublicData> getTransactions();
}
