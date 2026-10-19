package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.GetCommissionOrderRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCommissionOrderUseCaseImplTest {

    @Mock
    CommissionOrderRepository commissionOrderRepositoryMock;

    @InjectMocks
    GetCommissionOrderUseCaseImpl getCommissionOrderUseCaseImpl;

    @Test
    void getCommissionOrder_validInput_shouldReturnCommissionOrder() {
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

        var request = GetCommissionOrderRequest.builder().id(1L).build();

        var result = getCommissionOrderUseCaseImpl.getCommissionOrder(request);

        assertNotNull(result);
        verify(commissionOrderRepositoryMock).findById(1L);
    }

    @Test
    void getCommissionOrder_nullInput_shouldThrowException() {
        InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> getCommissionOrderUseCaseImpl.getCommissionOrder(null));
        assertTrue(ex.getMessage().contains("Invalid id"));

        verifyNoInteractions(commissionOrderRepositoryMock);
    }

    @Test
    void getCommissionOrder_invalidId_shouldThrowException() {
        var request = GetCommissionOrderRequest.builder().id(0L).build();
        InvalidInputException ex = assertThrows(InvalidInputException.class,
                () -> getCommissionOrderUseCaseImpl.getCommissionOrder(request));
        assertTrue(ex.getMessage().contains("Invalid id"));

        verifyNoInteractions(commissionOrderRepositoryMock);
    }

    @Test
    void getCommissionOrder_notFound_shouldThrowException() {
        when(commissionOrderRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        var request = GetCommissionOrderRequest.builder().id(1L).build();

        assertThrows(CommissionOrderNotFoundException.class,
                () -> getCommissionOrderUseCaseImpl.getCommissionOrder(request));

        verify(commissionOrderRepositoryMock).findById(1L);
    }
}
