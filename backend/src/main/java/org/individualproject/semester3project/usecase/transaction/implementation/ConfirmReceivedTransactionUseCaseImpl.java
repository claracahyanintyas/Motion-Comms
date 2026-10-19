package org.individualproject.semester3project.usecase.transaction.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.TransactionEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.TransactionRepository;
import org.individualproject.semester3project.usecase.exception.TransactionNotFound;
import org.individualproject.semester3project.usecase.transaction.ConfirmReceivedTransactionUseCase;
import org.individualproject.semester3project.usecase.transaction.dto.ConfirmReceivedTransactionInput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
@Transactional
public class ConfirmReceivedTransactionUseCaseImpl implements ConfirmReceivedTransactionUseCase {
    CommissionOrderRepository commissionOrderRepository;
    TransactionRepository transactionRepository;
    @Override
    public void confirm(ConfirmReceivedTransactionInput input) {
        var transaction = transactionRepository.findById(input.getOrderId())
                .orElseThrow(TransactionNotFound::new)
                .fromEntity();

        transaction.confirmReceived();
        transactionRepository.save(TransactionEntity.toEntity(transaction));

        var order = transaction.getCommissionOrder();

        if (transaction.getType() == TransactionType.DOWN_PAYMENT) {
            order.payDownPayment();
        } else {
            order.payRemaining();
        }

        commissionOrderRepository.save(CommissionOrderEntity.toEntity(order));
    }
}
