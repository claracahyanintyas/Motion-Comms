package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.exception.InvalidOrderStateException;
import org.individualproject.semester3project.domain.exception.UnauthorizedActionException;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.ConfirmOrderRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionOrderException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
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
class ConfirmOrderBySellerUseCaseImplTest {

    @Mock
    private CommissionOrderRepository commissionOrderRepository;

    @InjectMocks
    private ConfirmOrderBySellerUseCaseImpl confirmOrderBySellerUseCaseImpl;

    @Test
    void confirmBySeller_happyFlow_staysInProgress() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();


        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        confirmOrderBySellerUseCaseImpl.confirmBySeller(1L, ConfirmOrderRequest.builder().userId(user2.getId()).build());

        ArgumentCaptor<CommissionOrderEntity> captor =
                ArgumentCaptor.forClass(CommissionOrderEntity.class);

        verify(commissionOrderRepository).save(captor.capture());

        CommissionOrderEntity saved = captor.getValue();

        assertTrue(saved.isConfirmedBySeller());
        assertEquals(CommissionOrderStatus.PENDING_REMAINING_PAYMENT, saved.getCommissionOrderStatus());
    }

    @Test
    void confirmBySeller_happyFlow_pendingRemainingPayment() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();


        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        confirmOrderBySellerUseCaseImpl.confirmBySeller(1L, ConfirmOrderRequest.builder().userId(user2.getId()).build());

        ArgumentCaptor<CommissionOrderEntity> captor =
                ArgumentCaptor.forClass(CommissionOrderEntity.class);

        verify(commissionOrderRepository).save(captor.capture());

        CommissionOrderEntity saved = captor.getValue();

        assertTrue(saved.isConfirmedBySeller());
        assertEquals(CommissionOrderStatus.PENDING_REMAINING_PAYMENT, saved.getCommissionOrderStatus());
    }
    @Test
        void confirmBySeller_happyFlow_completed() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();
        order.confirmByBuyer(user1.getId());
        order.payRemaining();


        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        confirmOrderBySellerUseCaseImpl.confirmBySeller(1L, ConfirmOrderRequest.builder().userId(user2.getId()).build());

        ArgumentCaptor<CommissionOrderEntity> captor =
                ArgumentCaptor.forClass(CommissionOrderEntity.class);

        verify(commissionOrderRepository).save(captor.capture());

        CommissionOrderEntity saved = captor.getValue();

        assertTrue(saved.isConfirmedBySeller());
        assertEquals(CommissionOrderStatus.COMPLETED, saved.getCommissionOrderStatus());
    }
    @Test
    void confirmBySeller_happyFlow_pendingRemaining() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();
        order.confirmByBuyer(user1.getId());


        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        confirmOrderBySellerUseCaseImpl.confirmBySeller(1L, ConfirmOrderRequest.builder().userId(user2.getId()).build());

        ArgumentCaptor<CommissionOrderEntity> captor =
                ArgumentCaptor.forClass(CommissionOrderEntity.class);

        verify(commissionOrderRepository).save(captor.capture());

        CommissionOrderEntity saved = captor.getValue();

        assertTrue(saved.isConfirmedBySeller());
        assertEquals(CommissionOrderStatus.PENDING_REMAINING_PAYMENT, saved.getCommissionOrderStatus());
    }
    @Test
    void confirmBySeller_cancelledOrder_throwsException() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.setCommissionOrderStatus(CommissionOrderStatus.CANCELLED);

        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));

        InvalidOrderStateException ex = assertThrows(InvalidOrderStateException.class,
                () -> confirmOrderBySellerUseCaseImpl.confirmBySeller(
                        1L, ConfirmOrderRequest.builder().userId(user2.getId()).build()));
        assertTrue(ex.getMessage().contains("cannot send commission unless status is in progress"));

        verifyNoMoreInteractions(commissionOrderRepository);
    }
    @Test
    void confirmBySeller_wrongSellerId_throwException() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();

        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.of(CommissionOrderEntity.toEntity(order)));


        UnauthorizedActionException ex = assertThrows(UnauthorizedActionException.class,
                () -> confirmOrderBySellerUseCaseImpl.confirmBySeller(
                1L, ConfirmOrderRequest.builder().userId(user1.getId()).build()));
        assertTrue(ex.getMessage().contains("user is unauthorized"));

        verifyNoMoreInteractions(commissionOrderRepository);
    }
    @Test
    void confirmBySeller_invalidOrderId_throwException() {
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

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user1.fromEntity());
        order.payDownPayment();

        InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> confirmOrderBySellerUseCaseImpl.confirmBySeller(
                        -1L, ConfirmOrderRequest.builder().userId(user2.getId()).build()));
        assertTrue(ex.getMessage().contains("Invalid order id"));

        verifyNoInteractions(commissionOrderRepository);
    }
    @Test
    void confirmBySeller_orderNotFound_throwException() {

        UserEntity user2 = UserEntity.toEntity(User.builder()
                .id(2L)
                .username("name2")
                .email("email2")
                .passwordHash("password2")
                .build());

        when(commissionOrderRepository.findById(1L)).thenReturn(Optional.empty());

        InvalidCommissionOrderException ex = assertThrows(InvalidCommissionOrderException.class,
                () -> confirmOrderBySellerUseCaseImpl.confirmBySeller(
                        1L, ConfirmOrderRequest.builder().userId(user2.getId()).build()));
        assertTrue(ex.getMessage().contains("Commission order not found"));

        verifyNoMoreInteractions(commissionOrderRepository);
    }
}