package org.individualproject.semester3project.usecase.commission.implementation;

import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commission.dto.GetCommissionRequest;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionOutput;
import org.individualproject.semester3project.usecase.exception.CommissionNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCommissionUseCaseImplTest {

    @Mock
    private CommissionRepository commissionRepositoryMock;

    @InjectMocks
    private GetCommissionUseCaseImpl getCommissionUseCaseImpl;

    @Test
    void getCommission_should_return_right_value() {

        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        GetCommissionRequest request = GetCommissionRequest.builder().id(1L).build();
        CommissionEntity savedCommission = CommissionEntity.builder()
                .id(1L)
                .title("title")
                .description("description")
                .price(BigDecimal.valueOf(100.0))
                .commissionee(UserEntity.toEntity(commissionee))
                .build();

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(savedCommission));

        GetCommissionOutput actual = getCommissionUseCaseImpl.getCommission(request);

        assertEquals(1L,actual.getCommission().getId());
        assertEquals("title",actual.getCommission().getTitle());
        assertEquals("description",actual.getCommission().getDescription());
        assertEquals(1L,actual.getCommission().getCommissionee().getId());
        assertEquals("name", actual.getCommission().getCommissionee().getUsername());
        verify(commissionRepositoryMock).findById(1L);
        verifyNoMoreInteractions(commissionRepositoryMock);
    }
    @Test
    void getCommission_unexisting_commission_should_throw_error() {
        GetCommissionRequest request = GetCommissionRequest.builder().id(1L).build();

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        CommissionNotFoundException exception = assertThrows(CommissionNotFoundException.class, () ->
                getCommissionUseCaseImpl.getCommission(request));

        verify(commissionRepositoryMock).findById(1L);
        verifyNoMoreInteractions(commissionRepositoryMock);
        assertTrue(exception.getMessage().contains("Commission not found"));
    }
    @Test
    void getCommission_invalid_id_should_throw_error() {
        GetCommissionRequest request = GetCommissionRequest.builder().id(-1L).build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                getCommissionUseCaseImpl.getCommission(request));

        verifyNoInteractions(commissionRepositoryMock);
        assertTrue(exception.getMessage().contains("Commission id is invalid"));
    }
}