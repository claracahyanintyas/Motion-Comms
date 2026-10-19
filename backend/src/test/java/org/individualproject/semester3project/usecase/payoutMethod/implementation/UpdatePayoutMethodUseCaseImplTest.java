package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.UpdatePayoutMethodRequest;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePayoutMethodUseCaseImplTest {

    @Mock
    private PayoutMethodRepository payoutMethodRepository;

    @InjectMocks
    private UpdatePayoutMethodUseCaseImpl useCase;

    @Test
    void updatePayoutMethod_happyPath_shouldSaveUpdatedEntity() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("username")
                .build();

        PayoutMethodEntity entity = PayoutMethodEntity.builder()
                .id(1L)
                .accountName("Old")
                .accountNumber("111")
                .providerName("OldBank")
                .user(user)
                .build();

        when(payoutMethodRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        UpdatePayoutMethodRequest input = UpdatePayoutMethodRequest.builder()
                .accountName("NewName")
                .accountNumber("999")
                .providerName("NewBank")
                .build();

        // Act
        useCase.updatePayoutMethod(1L, input);

        // Assert
        verify(payoutMethodRepository).save(any(PayoutMethodEntity.class));
    }

    @Test
    void updatePayoutMethod_invalidId_shouldThrow() {
        assertThrows(InvalidInputException.class,
                () -> useCase.updatePayoutMethod(0L, UpdatePayoutMethodRequest.builder().build()));
    }

    @Test
    void updatePayoutMethod_notFound_shouldThrow() {
        when(payoutMethodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(PayoutMethodNotFound.class,
                () -> useCase.updatePayoutMethod(99L, UpdatePayoutMethodRequest.builder().build()));
    }
}
