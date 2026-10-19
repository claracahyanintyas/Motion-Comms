package org.individualproject.semester3project.usecase.commission.implementation;

import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.usecase.commission.dto.GetAllCommissionsOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCommissionsUseCaseImplTest {

    @Mock
    private CommissionRepository commissionRepositoryMock;

    @InjectMocks
    private GetAllCommissionsUseCaseImpl getAllCommissionsUseCaseImpl;

    @Test
    void getCommission_should_return_right_value() {

        User commissionee1 = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        User commissionee2 = User.builder()
                .id(2L)
                .username("name2")
                .passwordHash("password2")
                .build();

        CommissionEntity savedCommission1 = CommissionEntity.builder()
                .id(1L)
                .title("title")
                .description("description")
                .price(BigDecimal.valueOf(100.0))
                .commissionee(UserEntity.toEntity(commissionee1))
                .build();

        CommissionEntity savedCommission2 = CommissionEntity.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .price(BigDecimal.valueOf(12.0))
                .commissionee(UserEntity.toEntity(commissionee2))
                .build();

        List<CommissionEntity> savedCommissions = List.of(savedCommission1, savedCommission2);

        when(commissionRepositoryMock.findAll()).thenReturn(savedCommissions);

        GetAllCommissionsOutput actual = getAllCommissionsUseCaseImpl.getCommissions();

        assertNotNull(actual);
        assertEquals(2, actual.getCommissions().size());
        assertEquals(savedCommission1.getId(), actual.getCommissions().getFirst().getId());
        assertEquals("title", actual.getCommissions().get(0).getTitle());
        assertEquals("name", actual.getCommissions().get(0).getCommissionee().getUsername());
        assertEquals(savedCommission2.getId(), actual.getCommissions().get(1).getId());
        assertEquals("title2", actual.getCommissions().get(1).getTitle());
        assertEquals("name2", actual.getCommissions().get(1).getCommissionee().getUsername());
        verify(commissionRepositoryMock).findAll();
        verifyNoMoreInteractions(commissionRepositoryMock);

    }
}