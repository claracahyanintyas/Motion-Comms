package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.CreateCommissionOrderRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionException;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionOrderException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCommissionOrderUseCaseImplTest {

    @Mock
    CommissionOrderRepository commissionOrderRepositoryMock;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    CommissionRepository commissionRepositoryMock;

    @Mock
    PayoutMethodRepository payoutMethodRepositoryMock;

    @InjectMocks
    CreateCommissionOrderUseCaseImpl createCommissionOrderUseCaseImpl;

    @Test
    void createCommissionOrder_valid_input_should_return_correct_commissionOrderId() {
        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        User commissioner = User.builder()
                .id(2L)
                .username("name2")
                .passwordHash("password2")
                .build();

        UserEntity buyerEntity = UserEntity.toEntity(commissioner);

        Commission commission = Commission.builder()
                .id(1L)
                .commissionee(commissionee)
                .price(BigDecimal.valueOf(10.0))
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        CommissionEntity  commissionEntity = CommissionEntity.toEntity(commission);

        CommissionOrderEntity savedOrder = CommissionOrderEntity.builder()
                .id(1L)
                .commission(commissionEntity)
                .buyer(buyerEntity)
                .seller(UserEntity.toEntity(commissionee))
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .downPaymentAmount(BigDecimal.valueOf(2.0))
                .totalPrice(BigDecimal.valueOf(10.0))
                .remainingPaymentAmount(BigDecimal.valueOf(8.0))
                .build();

        PayoutMethodEntity payoutMethod = PayoutMethodEntity.builder().id(1L).user(UserEntity.toEntity(commissionee)).active(true).build();

        var methods = List.of(payoutMethod);

        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(2L)
                .build();

        when(userRepositoryMock.findById(2L)).thenReturn(Optional.ofNullable(buyerEntity));
        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(commissionEntity));
        when(commissionOrderRepositoryMock.save(any(CommissionOrderEntity.class))).thenReturn(savedOrder);
        when(payoutMethodRepositoryMock.findByUserIdAndActiveTrue(1L)).thenReturn(methods);



        var result = createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest);

        assertEquals(1L,result.getId());
        verify(userRepositoryMock).findById(2L);
        verify(commissionRepositoryMock).findById(1L);
        verify(commissionOrderRepositoryMock).save(any(CommissionOrderEntity.class));

    }

    @Test
    void createCommissionOrder_ongoingOrderExists_shouldThrowException() {

        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(2L)
                .build();

        when(commissionOrderRepositoryMock.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
                anyLong(),
                anyLong(),
                anyList()
        )).thenReturn(true);

        InvalidCommissionOrderException exception = assertThrows(InvalidCommissionOrderException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("You already have an active order for this commission"));

        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        verifyNoMoreInteractions(commissionOrderRepositoryMock);

    }


    @Test
    void CreateCommissionOrder_null_commissionId_input_should_throw_input_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(null)
                .buyerId(2L)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Input must not be null"));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        verifyNoInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_null_buyerId_input_should_throw_input_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(null)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Input must not be null"));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        verifyNoInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_null_input_should_throw_input_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = null;

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Input must not be null"));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        verifyNoInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_negative_input_should_throw_input_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(-1L)
                .build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Input must not be null and must be valid"));
        verifyNoInteractions(userRepositoryMock);
        verifyNoInteractions(commissionRepositoryMock);
        verifyNoInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_commission_not_found_should_throw_invalid_commission_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(2L)
                .build();

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        when(commissionOrderRepositoryMock.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
                anyLong(),
                anyLong(),
                anyList()
        )).thenReturn(false);

        InvalidCommissionException exception = assertThrows(InvalidCommissionException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Commission not found"));
        verifyNoMoreInteractions(commissionRepositoryMock);
        verifyNoInteractions(userRepositoryMock);
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_buyer_not_found_should_throw_invalid_commission_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(2L)
                .build();

        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        Commission commission = Commission.builder()
                .id(1L)
                .commissionee(commissionee)
                .price(BigDecimal.valueOf(12.0))
                .build();

        CommissionEntity  commissionEntity = CommissionEntity.toEntity(commission);

        PayoutMethodEntity payoutMethod = PayoutMethodEntity.builder().id(1L).user(UserEntity.toEntity(commissionee)).active(true).build();

        var methods = List.of(payoutMethod);

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(commissionEntity));
        when(userRepositoryMock.findById(2L)).thenReturn(Optional.empty());
        when(payoutMethodRepositoryMock.findByUserIdAndActiveTrue(1L)).thenReturn(methods);
        when(commissionOrderRepositoryMock.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
                anyLong(),
                anyLong(),
                anyList()
        )).thenReturn(false);

        InvalidUserException exception = assertThrows(InvalidUserException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("Buyer not found"));
        verifyNoMoreInteractions(commissionRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }
    @Test
    void CreateCommissionOrder_buyer_is_seller_should_throw_invalid_commission_exception() {
        CreateCommissionOrderRequest createCommissionOrderRequest = CreateCommissionOrderRequest.builder()
                .commissionId(1L)
                .buyerId(1L)
                .build();

        User commissionee = User.builder()
                .id(1L)
                .username("name")
                .passwordHash("password")
                .build();

        Commission commission = Commission.builder()
                .id(1L)
                .commissionee(commissionee)
                .price(BigDecimal.valueOf(12.0))
                .build();

        CommissionEntity  commissionEntity = CommissionEntity.toEntity(commission);

        when(commissionRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(commissionEntity));
        when(commissionOrderRepositoryMock.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
                anyLong(),
                anyLong(),
                anyList()
        )).thenReturn(false);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                createCommissionOrderUseCaseImpl.createCommissionOrder(createCommissionOrderRequest));

        assertTrue(exception.getMessage().contains("You cannot commission your own service"));
        verifyNoMoreInteractions(commissionRepositoryMock);
        verifyNoInteractions(userRepositoryMock);
        verifyNoMoreInteractions(commissionOrderRepositoryMock);
    }
}