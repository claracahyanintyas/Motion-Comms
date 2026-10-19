package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.exception.InvalidOrderStateException;
import org.individualproject.semester3project.domain.exception.UnauthorizedActionException;
import org.individualproject.semester3project.domain.transaction.model.Transaction;
import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.PayPaymentResponse;
import org.individualproject.semester3project.infrastructure.config.db.entity.TransactionEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.TransactionRepository;
import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentInput;
import org.individualproject.semester3project.usecase.commissionOrders.PayPaymentUseCase;
import org.individualproject.semester3project.usecase.commissionOrders.dto.PayPaymentOutput;
import org.individualproject.semester3project.usecase.exception.ActiveTransactionAlreadyExistsException;
import org.individualproject.semester3project.usecase.exception.CommissionOrderNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Transactional
@Service @AllArgsConstructor
public class PayPaymentUseCaseImpl implements PayPaymentUseCase {
    CommissionOrderRepository commissionOrderRepository;
    PayoutMethodRepository payoutMethodRepository;
    TransactionRepository transactionRepository;
    @Override
    public PayPaymentOutput pay(PayPaymentInput input) {
        if (input.getOrderId() == null){
            throw new InvalidInputException("Order id is mandatory");
        }
        if (input.getPayoutMethodId() == null){
            throw new InvalidInputException("Payout method id is mandatory");
        }
        if (input.getUserId() == null){
            throw new InvalidInputException("User id is mandatory");
        }
        var types = List.of(input.getType());
        var activeStatuses = List.of(TransactionStatus.PENDING, TransactionStatus.CONFIRMED);
        if (transactionRepository.existsByCommissionOrderIdAndTypeInAndStatusIn(input.getOrderId(), types, activeStatuses)){
            throw new ActiveTransactionAlreadyExistsException("There is already an active transaction with this order");
        }

        var order = commissionOrderRepository.findById(input.getOrderId())
                .orElseThrow(CommissionOrderNotFoundException::new)
                .fromEntity();

        if (!Objects.equals(input.getUserId(), order.getBuyer().getId())){
            throw new UnauthorizedActionException("payer is not the same as buyer of the order");
        }

        if (input.getType() == TransactionType.REMAINING_PAYMENT
                && !order.isDownPaymentPaid()) {
            throw new InvalidOrderStateException("Down payment must be confirmed first");
        }

        var payoutMethod = payoutMethodRepository.findById(input.getPayoutMethodId())
                .orElseThrow(PayoutMethodNotFound::new)
                .fromEntity();
        var transaction = Transaction.builder()
                .commissionOrder(order)
                .payoutMethod(payoutMethod)
                .type(input.getType())
                .amount(input.getAmount())
                .proofUrl(input.getProofUrl())
                .build();
        var savedTransaction = transactionRepository.save(TransactionEntity.toEntity(transaction));
        return PayPaymentResponse.builder()
                .id(savedTransaction.getId()).build();
    }

}
