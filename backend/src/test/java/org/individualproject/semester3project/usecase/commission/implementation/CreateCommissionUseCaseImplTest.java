package org.individualproject.semester3project.usecase.commission.implementation;

import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commission.dto.CreateCommissionRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommissionUseCaseImplTest {

    @Mock
    CommissionRepository commissionRepositoryMock;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    PayoutMethodRepository payoutMethodRepositoryMock;

    @InjectMocks
    CreateCommissionUseCaseImpl createCommissionUseCaseImpl;

    @Test
    void createCommission_valid_input_should_return_correct_commissionId() {
        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        UserEntity userEntity = UserEntity.toEntity(commissionee);

        PayoutMethodEntity payoutMethod = PayoutMethodEntity.builder().id(1L).user(userEntity).active(true).build();

        CreateCommissionRequest createCommissionRequest = CreateCommissionRequest.builder()
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissioneeId(1L)
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        CommissionEntity savedSchema = CommissionEntity.builder()
                .id(1L)
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissionee(userEntity)
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        var methods = List.of(payoutMethod);

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(userEntity));
        when(commissionRepositoryMock.save(any(CommissionEntity.class))).thenReturn(savedSchema);
        when(payoutMethodRepositoryMock.findByUserIdAndActiveTrue(1L)).thenReturn(methods);


        var result = createCommissionUseCaseImpl.createCommission(createCommissionRequest);

        assertEquals(1L,result.getId());
        verify(userRepositoryMock).findById(1L);
        verify(commissionRepositoryMock).save(any(CommissionEntity.class));
    }
    @Test
    void createCommission_unexisting_user_should_throw_exception() {

        CreateCommissionRequest createCommissionRequest = CreateCommissionRequest.builder()
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissioneeId(1L)
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        InvalidUserException exception = assertThrows(InvalidUserException.class, () ->
                createCommissionUseCaseImpl.createCommission(createCommissionRequest));

        verify(userRepositoryMock).findById(1L);
        verifyNoInteractions(commissionRepositoryMock);
        assertTrue(exception.getMessage().contains("commissionee not found"));
    }
    @Test
    void createCommission_invalid_commissioneeId_should_throw_exception() {

        CreateCommissionRequest createCommissionRequest = CreateCommissionRequest.builder()
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissioneeId(-1L)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionUseCaseImpl.createCommission(createCommissionRequest));

        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        assertTrue(exception.getMessage().contains("commissionee id is invalid"));
    }
}