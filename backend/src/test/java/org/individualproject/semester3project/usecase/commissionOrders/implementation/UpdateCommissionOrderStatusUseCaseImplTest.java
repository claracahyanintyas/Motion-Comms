package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.UpdateCommissionOrderStatusRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.exception.CommissionOrderNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class  UpdateCommissionOrderStatusUseCaseImplTest {

    @Mock
    CommissionOrderRepository commissionOrderRepositoryMock;

    @InjectMocks
    UpdateCommissionOrderStatusUseCaseImpl updateCommissionOrderStatusUseCaseImpl;

    @Test
    void updateCommissionOrderStatus_validInput_shouldUpdateStatus() {
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

        CommissionEntity commission2 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12.0))
                .title("title2")
                .build();

        CommissionOrderEntity commissionOrderEntity = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        CommissionOrderEntity savedOrder = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.IN_PROGRESS)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();


        when(commissionOrderRepositoryMock.findById(1L)).thenReturn(Optional.of(commissionOrderEntity));
        when(commissionOrderRepositoryMock.save(any(CommissionOrderEntity.class))).thenReturn(savedOrder);

        var request = UpdateCommissionOrderStatusRequest.builder()
                .commissionOrderStatus(CommissionOrderStatus.IN_PROGRESS).build();

        updateCommissionOrderStatusUseCaseImpl.updateCommissionOrderStatus(1L, request);

        verify(commissionOrderRepositoryMock).findById(1L);
        verify(commissionOrderRepositoryMock).save(any(CommissionOrderEntity.class));
    }

    @Test
    void updateCommissionOrderStatus_orderIdInvalid_shouldThrowException() {
        var  request = UpdateCommissionOrderStatusRequest.builder().commissionOrderStatus(CommissionOrderStatus.IN_PROGRESS).build();

        Exception exception = assertThrows(InvalidInputException.class,
                () -> updateCommissionOrderStatusUseCaseImpl.updateCommissionOrderStatus(0L, request));

        assertEquals("Invalid order ID", exception.getMessage());
        verifyNoInteractions(commissionOrderRepositoryMock);
        verify(commissionOrderRepositoryMock, never()).save(any());
    }

    @Test
    void updateCommissionOrderStatus_orderNotFound_shouldThrowException() {
        when(commissionOrderRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        var  request = UpdateCommissionOrderStatusRequest.builder().commissionOrderStatus(CommissionOrderStatus.IN_PROGRESS).build();

        Exception exception = assertThrows(CommissionOrderNotFoundException.class,
                () -> updateCommissionOrderStatusUseCaseImpl.updateCommissionOrderStatus(1L, request));

        assertEquals("Commission order not found", exception.getMessage());
        verify(commissionOrderRepositoryMock).findById(1L);
        verify(commissionOrderRepositoryMock, never()).save(any());
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }

    @Test
    void updateCommissionOrderStatus_nullInput_shouldDoNothing() {
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

        CommissionEntity commission2 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12.0))
                .title("title2")
                .build();

        CommissionOrderEntity commissionOrderEntity = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        when(commissionOrderRepositoryMock.findById(1L)).thenReturn(Optional.of(commissionOrderEntity));


        updateCommissionOrderStatusUseCaseImpl.updateCommissionOrderStatus(1L, null);

        verify(commissionOrderRepositoryMock).findById(1L);
        verify(commissionOrderRepositoryMock, never()).save(any());
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }

    @Test
    void updateCommissionOrderStatus_nullStatus_shouldDoNothing() {
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

        CommissionEntity commission2 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12.0))
                .title("title2")
                .build();

        CommissionOrderEntity commissionOrderEntity = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        when(commissionOrderRepositoryMock.findById(1L)).thenReturn(Optional.of(commissionOrderEntity));

        var request = UpdateCommissionOrderStatusRequest.builder().commissionOrderStatus(null).build();

        updateCommissionOrderStatusUseCaseImpl.updateCommissionOrderStatus(1L, request);

        assertEquals(CommissionOrderStatus.PENDING_DOWN_PAYMENT, commissionOrderEntity.getCommissionOrderStatus());
        verify(commissionOrderRepositoryMock, never()).save(any());
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }
}
