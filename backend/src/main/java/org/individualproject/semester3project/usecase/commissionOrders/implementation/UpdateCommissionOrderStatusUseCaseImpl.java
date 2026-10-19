package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.commissionOrders.UpdateCommissionOrderStatusUseCase;
import org.individualproject.semester3project.usecase.commissionOrders.dto.UpdateCommissionOrderStatusInput;
import org.individualproject.semester3project.usecase.exception.CommissionOrderNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class UpdateCommissionOrderStatusUseCaseImpl implements UpdateCommissionOrderStatusUseCase {

    private CommissionOrderRepository commissionOrderRepository;
    @Override
    public void updateCommissionOrderStatus(Long orderId, UpdateCommissionOrderStatusInput input) {

        if (orderId < 1 )
        {
            throw new InvalidInputException("Invalid order ID");
        }

        CommissionOrder order = commissionOrderRepository.findById(orderId)
                .orElseThrow(CommissionOrderNotFoundException::new)
                .fromEntity();

        if (input == null || input.getCommissionOrderStatus() == null) {
            return;
        }
        order.setCommissionOrderStatus(input.getCommissionOrderStatus());
        commissionOrderRepository.save(CommissionOrderEntity.toEntity(order));
    }
}
