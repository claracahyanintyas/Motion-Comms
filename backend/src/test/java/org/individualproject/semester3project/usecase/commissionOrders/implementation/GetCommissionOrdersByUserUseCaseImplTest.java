package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.GetCommissionOrdersByUserRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCommissionOrdersByUserUseCaseImplTest {

    @Mock
    private CommissionOrderRepository commissionOrderRepository;
    @InjectMocks
    private GetCommissionOrdersByUserUseCaseImpl getCommissionOrdersByUserUseCaseImpl;

    @Test
    void getCommissionOrdersByUser_null_role_should_return_all_as_seller_and_buyer() {
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

        CommissionEntity commission1 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user1)
                .price(BigDecimal.valueOf(12.0))
                .title("title")
                .build();

        CommissionEntity commission2 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user2)
                .price(BigDecimal.valueOf(12.0))
                .title("title2")
                .build();

        CommissionOrderEntity commissionOrderEntity1 = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission1)
                .buyer(user2)
                .seller(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        CommissionOrderEntity commissionOrderEntity2 = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        List<CommissionOrderEntity> entityList = List.of(commissionOrderEntity1,commissionOrderEntity2);

        List<CommissionOrder> expected = List.of(commissionOrderEntity1.fromEntity(), commissionOrderEntity2.fromEntity());

        GetCommissionOrdersByUserRequest request = new GetCommissionOrdersByUserRequest(1L, null);

        when(commissionOrderRepository.findAllByUserId(1L)).thenReturn(entityList);

        var result = getCommissionOrdersByUserUseCaseImpl.getCommissionOrdersByUser(request);

        assertNotNull(result);
        assertEquals(2, result.getCommissionOrders().size());
        assertEquals(expected.getFirst().getCommission().getId(), result.getCommissionOrders().getFirst().getCommission().getId());
        assertEquals(expected.getFirst().getSeller().getId(), result.getCommissionOrders().getFirst().getSeller().getId());
        assertEquals(expected.getFirst().getBuyer().getId(), result.getCommissionOrders().getFirst().getBuyer().getId());
        assertEquals(expected.getFirst().getCommission().getTitle(), result.getCommissionOrders().getFirst().getCommission().getTitle());
        assertEquals(expected.get(1).getCommission().getId(), result.getCommissionOrders().get(1).getCommission().getId());
        assertEquals(expected.get(1).getSeller().getId(), result.getCommissionOrders().get(1).getSeller().getId());
        assertEquals(expected.get(1).getBuyer().getId(), result.getCommissionOrders().get(1).getBuyer().getId());
        verify(commissionOrderRepository).findAllByUserId(1L);
        verifyNoMoreInteractions(commissionOrderRepository);
    }
    @Test
    void getCommissionOrdersByUser_buyer_role_should_only_return_orders_as_buyer() {
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

        CommissionOrderEntity commissionOrderEntity2 = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission2)
                .buyer(user1)
                .seller(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        List<CommissionOrderEntity> buyerList = List.of(commissionOrderEntity2);

        List<CommissionOrder> expected = List.of(commissionOrderEntity2.fromEntity());

        GetCommissionOrdersByUserRequest request = new GetCommissionOrdersByUserRequest(1L, "buyer");

        when(commissionOrderRepository.findByUserRole(1L, "buyer")).thenReturn(buyerList);

        var result = getCommissionOrdersByUserUseCaseImpl.getCommissionOrdersByUser(request);

        assertNotNull(result);
        assertEquals(1, result.getCommissionOrders().size());
        assertEquals(expected.getFirst().getCommission().getId(), result.getCommissionOrders().getFirst().getCommission().getId());
        assertEquals(expected.getFirst().getSeller().getId(), result.getCommissionOrders().getFirst().getSeller().getId());
        assertEquals(expected.getFirst().getBuyer().getId(), result.getCommissionOrders().getFirst().getBuyer().getId());
        assertEquals(expected.getFirst().getCommission().getTitle(), result.getCommissionOrders().getFirst().getCommission().getTitle());
        verify(commissionOrderRepository).findByUserRole(1L, "buyer");
        verifyNoMoreInteractions(commissionOrderRepository);
    }
    @Test
    void getCommissionOrdersByUser_seller_role_should_only_return_orders_as_seller() {
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

        CommissionEntity commission1 = CommissionEntity.builder()
                .id(1L)
                .commissionee(user1)
                .price(BigDecimal.valueOf(12.0))
                .title("title")
                .build();

        CommissionOrderEntity commissionOrderEntity1 = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commission1)
                .buyer(user2)
                .seller(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now()).build();

        List<CommissionOrderEntity> sellerList = List.of(commissionOrderEntity1);

        List<CommissionOrder> expected = List.of(commissionOrderEntity1.fromEntity());

        GetCommissionOrdersByUserRequest request = new GetCommissionOrdersByUserRequest(1L, "seller");

        when(commissionOrderRepository.findByUserRole(1L, "seller")).thenReturn(sellerList);

        var result = getCommissionOrdersByUserUseCaseImpl.getCommissionOrdersByUser(request);

        assertNotNull(result);
        assertEquals(1, result.getCommissionOrders().size());
        assertEquals(expected.getFirst().getCommission().getId(), result.getCommissionOrders().getFirst().getCommission().getId());
        assertEquals(expected.getFirst().getSeller().getId(), result.getCommissionOrders().getFirst().getSeller().getId());
        assertEquals(expected.getFirst().getBuyer().getId(), result.getCommissionOrders().getFirst().getBuyer().getId());
        assertEquals(expected.getFirst().getCommission().getTitle(), result.getCommissionOrders().getFirst().getCommission().getTitle());
        verify(commissionOrderRepository).findByUserRole(1L, "seller");
        verifyNoMoreInteractions(commissionOrderRepository);
    }
    @Test
    void getCommissionOrdersByUser_empty_userId_throws_exception() {
        GetCommissionOrdersByUserRequest request = new GetCommissionOrdersByUserRequest(null, null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                getCommissionOrdersByUserUseCaseImpl.getCommissionOrdersByUser(request));

        assertTrue(exception.getMessage().contains("User id cannot be empty"));
        verifyNoInteractions(commissionOrderRepository);
    }
    @Test
    void getCommissionOrdersByUser_invalid_userId_throws_exception() {
        GetCommissionOrdersByUserRequest request = new GetCommissionOrdersByUserRequest(-1L, null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                getCommissionOrdersByUserUseCaseImpl.getCommissionOrdersByUser(request));

        assertTrue(exception.getMessage().contains("User id cannot be empty or invalid"));
        verifyNoInteractions(commissionOrderRepository);
    }
}