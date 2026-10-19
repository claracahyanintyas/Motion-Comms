package org.individualproject.semester3project.usecase.mapper;

import org.individualproject.semester3project.domain.transaction.model.Transaction;
import org.individualproject.semester3project.infrastructure.transaction.dto.TransactionPublicDataImpl;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;
import org.individualproject.semester3project.usecase.transaction.dto.TransactionPublicData;

public class TransactionMapper {
    public static TransactionPublicData toTransactionPublicData(Transaction transaction) {
        CommissionOrderPublicData order = CommissionOrderMapper.ToCommissionPublicData(transaction.getCommissionOrder());
        PayoutMethodPublicData payoutMethod =  PayoutMethodMapper.toPayoutMethodPublicData(transaction.getPayoutMethod());
        return TransactionPublicDataImpl.builder()
                .id(transaction.getId())
                .commissionOrder(order)
                .payoutMethod(payoutMethod)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .confirmedAt(transaction.getConfirmedAt())
                .createdAt(transaction.getCreatedAt())
                .status(transaction.getStatus())
                .proofUrl(transaction.getProofUrl())
                .build();
    }
}
