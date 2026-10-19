package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.CreatePayoutMethodRequest;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodOutput;
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
class CreatePayoutMethodUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PayoutMethodRepository payoutMethodRepository;

    @InjectMocks
    private CreatePayoutMethodUseCaseImpl useCase;

    @Test
    void createPayoutMethod_happyPath_shouldReturnId() {
        // arrange
        CreatePayoutMethodRequest input = CreatePayoutMethodRequest.builder()
                .userId(1L)
                .type(PayoutMethodType.BANK_ACCOUNT)
                .accountName("John")
                .accountNumber("123456")
                .providerName("ING")
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        PayoutMethodEntity savedEntity = new PayoutMethodEntity();
        savedEntity.setId(10L);

        when(payoutMethodRepository.save(any()))
                .thenReturn(savedEntity);

        // act
        CreatePayoutMethodOutput output = useCase.createPayoutMethod(input);

        // assert
        assertEquals(10L, output.getId());
        verify(userRepository).findById(1L);
        verify(payoutMethodRepository).save(any(PayoutMethodEntity.class));
    }

    @Test
    void createPayoutMethod_nullUserId_shouldThrow() {
        CreatePayoutMethodRequest input = CreatePayoutMethodRequest.builder()
                .type(PayoutMethodType.BANK_ACCOUNT)
                .accountName("John")
                .accountNumber("123")
                .build();

        assertThrows(InvalidUserException.class,
                () -> useCase.createPayoutMethod(input));
    }

    @Test
    void createPayoutMethod_userNotFound_shouldThrow() {
        CreatePayoutMethodRequest input = CreatePayoutMethodRequest.builder()
                .userId(99L)
                .type(PayoutMethodType.BANK_ACCOUNT)
                .accountName("John")
                .accountNumber("123")
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class,
                () -> useCase.createPayoutMethod(input));
    }

    @Test
    void createPayoutMethod_nullType_shouldThrow() {
        CreatePayoutMethodRequest input = CreatePayoutMethodRequest.builder()
                .userId(1L)
                .accountName("John")
                .accountNumber("123")
                .build();

        assertThrows(InvalidInputException.class,
                () -> useCase.createPayoutMethod(input));
    }
}
