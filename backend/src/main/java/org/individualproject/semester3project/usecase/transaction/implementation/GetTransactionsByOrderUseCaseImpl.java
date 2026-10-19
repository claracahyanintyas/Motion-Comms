package org.individualproject.semester3project.usecase.transaction.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.entity.TransactionEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.TransactionRepository;
import org.individualproject.semester3project.infrastructure.transaction.dto.GetTransactionsByOrderResponse;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.mapper.TransactionMapper;
import org.individualproject.semester3project.usecase.transaction.GetTransactionsByOrderUseCase;
import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderInput;
import org.individualproject.semester3project.usecase.transaction.dto.GetTransactionsByOrderOutput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class GetTransactionsByOrderUseCaseImpl implements GetTransactionsByOrderUseCase {
    private TransactionRepository transactionRepository;
    private CommissionOrderRepository commissionOrderRepository;

    @Override
    public GetTransactionsByOrderOutput getTransactionsByOrder(GetTransactionsByOrderInput input) {
        if (input == null || input.getOrderId() == null || input.getOrderId() <= 0) {
            throw new InvalidInputException("Input cannot be null and id cannot be below 1");
        }
        var transactions = transactionRepository.findAllByCommissionOrderId(input.getOrderId())
                .stream().map(TransactionEntity::fromEntity).toList();
        return GetTransactionsByOrderResponse.builder()
                .transactions(transactions.stream().map(TransactionMapper::toTransactionPublicData).toList())
                .build();
    }
}
