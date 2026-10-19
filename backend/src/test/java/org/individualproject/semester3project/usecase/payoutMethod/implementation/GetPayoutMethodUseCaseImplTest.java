package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.GetPayoutMethodRequest;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPayoutMethodUseCaseImplTest {

    @Mock
    private PayoutMethodRepository payoutMethodRepository;

    @InjectMocks
    private GetPayoutMethodUseCaseImpl useCase;

    @Test
    void getPayoutMethod_happyPath_shouldReturnPublicData() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("John").build();
        PayoutMethodEntity entity = PayoutMethodEntity.builder()
                .id(1L)
                .accountName("John")
                .accountNumber("123")
                .providerName("ING")
                .user(user)
                .build();

        when(payoutMethodRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        GetPayoutMethodRequest input =
                GetPayoutMethodRequest.builder()
                        .id(1L)
                        .build();

        // Act
        var output = useCase.getPayoutMethod(input);

        // Assert
        assertNotNull(output);
        assertEquals("John", output.getPayoutMethod().getAccountName());
    }

    @Test
    void getPayoutMethod_invalidId_shouldThrow() {
        assertThrows(InvalidInputException.class,
                () -> useCase.getPayoutMethod(
                        GetPayoutMethodRequest.builder().id(0L).build()
                ));
    }

    @Test
    void getPayoutMethod_notFound_shouldThrow() {
        when(payoutMethodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(PayoutMethodNotFound.class,
                () -> useCase.getPayoutMethod(
                        GetPayoutMethodRequest.builder().id(99L).build()
                ));
    }
}
