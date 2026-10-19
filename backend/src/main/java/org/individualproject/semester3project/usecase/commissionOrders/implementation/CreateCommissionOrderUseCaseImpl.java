package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.CreateCommissionOrderResponse;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.usecase.commissionOrders.CreateCommissionOrderUseCase;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderOutput;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionException;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionOrderException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class CreateCommissionOrderUseCaseImpl implements CreateCommissionOrderUseCase {
    private CommissionOrderRepository commissionOrderRepository;
    private UserRepository userRepository;
    private CommissionRepository commissionRepository;
    private PayoutMethodRepository payoutMethodRepository;
    public CreateCommissionOrderOutput createCommissionOrder(CreateCommissionOrderInput input ) {
        if (input == null || input.getCommissionId() == null || input.getBuyerId() == null || input.getCommissionId() <= 0 || input.getBuyerId() <= 0) {
            throw new InvalidInputException("Input must not be null and must be valid");
        }
        var ACTIVE_STATUSES = List.of(
                CommissionOrderStatus.AWAITING_CONFIRMATION,
                CommissionOrderStatus.IN_PROGRESS,
                CommissionOrderStatus.PENDING_DOWN_PAYMENT,
                CommissionOrderStatus.PENDING_REMAINING_PAYMENT
        );
        if (commissionOrderRepository.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
                input.getBuyerId(),
                input.getCommissionId(),
                ACTIVE_STATUSES
        )) {
            throw new InvalidCommissionOrderException(
                    "You already have an active order for this commission"
            );
        }
        Commission commission = commissionRepository.findById(input.getCommissionId())
                .orElseThrow(() -> new InvalidCommissionException("Commission not found"))
                .fromEntity();
        //checking whether the buyer and seller are the same
        if (commission.getCommissionee().getId().equals(input.getBuyerId())) {
            throw new InvalidInputException("You cannot commission your own service.");
        }
        //checking whether commissionee has any active payout method\
        var payoutMethods = payoutMethodRepository.findByUserIdAndActiveTrue(commission.getCommissionee().getId());
        if (payoutMethods.isEmpty()) {
            throw new InvalidUserException("Commissionee / seller has no active payout method");
        }
        User buyer = userRepository.findById(input.getBuyerId())
                .orElseThrow(() -> new InvalidUserException("Buyer not found"))
                .fromEntity();
        var commissionOrder = new CommissionOrder(commission, buyer);
        CommissionOrderEntity commissionOrderEntity = commissionOrderRepository.save(CommissionOrderEntity.toEntity(commissionOrder));
        return CreateCommissionOrderResponse.builder()
                .id(commissionOrderEntity.getId())
                .build();
    }
}
