package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.GetActivePayoutMethodsByUserRequest;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetActivePayoutMethodsByUserUseCaseImplTest {

    @Mock
    private PayoutMethodRepository payoutMethodRepository;

    @InjectMocks
    private GetActivePayoutMethodsByUserUseCaseImpl useCase;

    @Test
    void getActivePayoutMethods_happyPath_shouldReturnList() {
        // Arrange
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("username")
                .build();

        PayoutMethodEntity entity = PayoutMethodEntity.builder()
                .id(10L)
                .accountName("John")
                .accountNumber("123")
                .providerName("ING")
                .user(user)
                .active(true)
                .build();

        when(payoutMethodRepository.findByUserIdAndActiveTrue(user.getId())).thenReturn(List.of(entity));

        GetActivePayoutMethodsByUserRequest input =
                GetActivePayoutMethodsByUserRequest.builder()
                        .userId(user.getId())
                        .build();

        // Act
        var output = useCase.getActivePayoutMethodByUser(input);

        // Assert
        assertNotNull(output);
        assertEquals(1, output.getPayoutMethods().size());
        assertEquals("John", output.getPayoutMethods().get(0).getAccountName());
    }

    @Test
    void getActivePayoutMethods_invalidUserId_shouldThrow() {
        // Act + Assert
        assertThrows(InvalidInputException.class,
                () -> useCase.getActivePayoutMethodByUser(
                        GetActivePayoutMethodsByUserRequest.builder()
                                .userId(0L)
                                .build()
                ));
    }
}
