package org.individualproject.semester3project.usecase.commission.implementation;

import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commission.dto.UpdateCommissionRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.usecase.exception.CommissionNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCommissionUseCaseImplTest {

    @Mock
    CommissionRepository commissionRepositoryMock;


    @InjectMocks
    UpdateCommissionUseCaseImpl updateCommissionUseCase;

    @Test
    void updateCommission_commissionee_exists_return_nothing() {
        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        Commission commission = Commission.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissionee(commissionee)
                .build();

        UpdateCommissionRequest request = UpdateCommissionRequest.builder()
                .title("new title")
                .description("new desc")
                .price(BigDecimal.valueOf(4.0))
                .downPaymentPercentage(BigDecimal.valueOf(5.0))
                .build();

        Commission updatedComm = Commission.builder()
                .id(1L)
                .commissionee(commissionee)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .downPaymentPercentage(BigDecimal.valueOf(0.5))
                .build();

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.of(CommissionEntity.toEntity(commission)));
        when(commissionRepositoryMock.save(any())).thenReturn(CommissionEntity.toEntity(updatedComm));
        updateCommissionUseCase.updateCommission(commission.getId(), request);

        verify(commissionRepositoryMock).findById(1L);
        verify(commissionRepositoryMock).save(any());
    }
    @Test
    void updateCommission_unexisting_commissionee_throws_exception() {

        UpdateCommissionRequest request = UpdateCommissionRequest.builder()
                .title("new title")
                .description("new desc")
                .price(BigDecimal.valueOf(4.0)).build();

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        CommissionNotFoundException exception = assertThrows(CommissionNotFoundException.class, () ->
                updateCommissionUseCase.updateCommission(1L, request));

        verify(commissionRepositoryMock).findById(1L);
        verifyNoMoreInteractions(commissionRepositoryMock);
        assertEquals("Commission not found", exception.getMessage());
    }
}