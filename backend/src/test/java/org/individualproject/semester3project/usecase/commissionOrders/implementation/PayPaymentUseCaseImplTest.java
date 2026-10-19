package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.exception.InvalidOrderStateException;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.PayPaymentRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.*;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.TransactionRepository;
import org.individualproject.semester3project.usecase.exception.ActiveTransactionAlreadyExistsException;
import org.individualproject.semester3project.usecase.exception.CommissionOrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayPaymentUseCaseImplTest {

    @Mock
    private CommissionOrderRepository commissionOrderRepository;

    @Mock
    private PayoutMethodRepository payoutMethodRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PayPaymentUseCaseImpl payPaymentUseCase;

    @Test
    void payDownPayment_HappyFlow() {
        UserEntity user1 = UserEntity.toEntity(User.builder()
                .id(1L)
                .username("name")
                .email("email")
                .passwordHash("password")
                .build());

        UserEntity user2 = UserEntity.toEntity(User.builder()
                .id(2L)
                .username("name2")
                .email("email2")
                .passwordHash("password2")
                .build());

        CommissionEntity commission = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12.0))
                .title("title2")
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        CommissionOrder order =
                new CommissionOrder(commission.fromEntity(), user1.fromEntity());

        PayoutMethod payoutMethod = PayoutMethod.builder()
                .id(1L)
                .accountName("name")
                .accountNumber("129380")
                .type(PayoutMethodType.MOBILE_WALLET)
                .providerName("providerName")
                .user(user2.fromEntity())
                .build();

        PayPaymentRequest paymentRequest = PayPaymentRequest.builder()
                .type(TransactionType.DOWN_PAYMENT)
                .orderId(1L)
                .amount(order.getDownPaymentAmount())
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(user1.getId())
                .build();

        when(commissionOrderRepository.findById(1L))
                .thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        when(payoutMethodRepository.findById(1L))
                .thenReturn(Optional.of(PayoutMethodEntity.toEntity(payoutMethod)));

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<TransactionEntity> transactionCaptor =
                ArgumentCaptor.forClass(TransactionEntity.class);

        // ===== Act =====
        payPaymentUseCase.pay(paymentRequest);

        // ===== Assert =====

        verify(transactionRepository).save(transactionCaptor.capture());

        TransactionEntity savedTransaction = transactionCaptor.getValue();

        // Transaction assertions
        assertEquals(TransactionType.DOWN_PAYMENT, savedTransaction.getType());
        assertEquals(order.getDownPaymentAmount(), savedTransaction.getAmount());
        assertEquals("url", savedTransaction.getProofUrl());
        assertEquals(payoutMethod.getId(), savedTransaction.getPayoutMethod().getId());
    }

    @Test
    void payDownPayment_UnhappyFlow_NotFound() {
        when(commissionOrderRepository.findById(1L))
                .thenReturn(Optional.empty());

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.DOWN_PAYMENT)
                .orderId(1L)
                .amount(BigDecimal.TEN)
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(1L)
                .build();

        when(transactionRepository.existsByCommissionOrderIdAndTypeInAndStatusIn(any(), any(), any())).thenReturn(false);
        assertThrows(CommissionOrderNotFoundException.class,
                () -> payPaymentUseCase.pay(request));

        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void payRemaining_happyFlow() {
        // Arrange
        UserEntity user1 = UserEntity.toEntity(User.builder()
                .id(1L)
                .username("name")
                .email("email")
                .passwordHash("password")
                .build());

        UserEntity user2 = UserEntity.toEntity(User.builder()
                .id(2L)
                .username("name2")
                .email("email2")
                .passwordHash("password2")
                .build());
        CommissionEntity commission = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12))
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        PayoutMethod payoutMethod = PayoutMethod.builder()
                .id(1L)
                .accountName("name")
                .accountNumber("129380")
                .type(PayoutMethodType.MOBILE_WALLET)
                .providerName("providerName")
                .user(user2.fromEntity())
                .build();



        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment(); // required precondition

        when(commissionOrderRepository.findById(1L))
                .thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        when(payoutMethodRepository.findById(1L))
                .thenReturn(Optional.of(PayoutMethodEntity.toEntity(payoutMethod)));

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .orderId(1L)
                .amount(order.getRemainingPaymentAmount())
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(user1.getId())
                .build();

        ArgumentCaptor<TransactionEntity> transactionCaptor =
                ArgumentCaptor.forClass(TransactionEntity.class);

        // Act
        payPaymentUseCase.pay(request);

        // Assert
        verify(transactionRepository).save(transactionCaptor.capture());

        TransactionEntity tx = transactionCaptor.getValue();
        assertEquals(TransactionType.REMAINING_PAYMENT, tx.getType());
        assertEquals(order.getRemainingPaymentAmount(), tx.getAmount());
    }

    @Test
    void payRemaining_unhappyFlow_noDownPayment_invalidStatusException() {
        UserEntity user1 = UserEntity.toEntity(User.builder().id(1L).username("name").build());
        UserEntity user2 = UserEntity.toEntity(User.builder().id(2L).username("name2").build());

        CommissionEntity commission = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12))
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());

        when(commissionOrderRepository.findById(1L))
                .thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));
        when(transactionRepository.existsByCommissionOrderIdAndTypeInAndStatusIn(any(), any(), any())).thenReturn(false);

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .orderId(1L)
                .amount(order.getRemainingPaymentAmount())
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(user1.getId())
                .build();

        assertThrows(InvalidOrderStateException.class,
                () -> payPaymentUseCase.pay(request));

        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void payRemaining_UnhappyFlow_NotFound() {
        when(commissionOrderRepository.findById(1L))
                .thenReturn(Optional.empty());

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .orderId(1L)
                .amount(BigDecimal.TEN)
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(1L)
                .build();

        when(transactionRepository.existsByCommissionOrderIdAndTypeInAndStatusIn(any(), any(), any())).thenReturn(false);
        assertThrows(CommissionOrderNotFoundException.class,
                () -> payPaymentUseCase.pay(request));

        verifyNoMoreInteractions(transactionRepository);
    }
    @Test
    void payRemaining_OngoingTransaction_throwsException() {
        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .orderId(1L)
                .amount(BigDecimal.TEN)
                .proofUrl("url")
                .payoutMethodId(1L)
                .userId(1L)
                .build();

        when(transactionRepository.existsByCommissionOrderIdAndTypeInAndStatusIn(any(), any(), any())).thenReturn(true);
        assertThrows(ActiveTransactionAlreadyExistsException.class,
                () -> payPaymentUseCase.pay(request));

        verifyNoMoreInteractions(transactionRepository);
    }
}
