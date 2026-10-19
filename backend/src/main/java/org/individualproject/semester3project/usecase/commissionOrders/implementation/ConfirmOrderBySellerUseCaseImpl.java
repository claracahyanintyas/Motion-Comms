package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.commissionOrders.ConfirmOrderBySellerUseCase;
import org.individualproject.semester3project.usecase.commissionOrders.dto.ConfirmOrderInput;
import org.individualproject.semester3project.usecase.exception.InvalidCommissionOrderException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class ConfirmOrderBySellerUseCaseImpl implements ConfirmOrderBySellerUseCase {

    CommissionOrderRepository commissionOrderRepository;

    @Override
    public void confirmBySeller(Long orderId, ConfirmOrderInput input) {
        if (orderId == null || orderId <= 0) {
            throw new InvalidInputException("Invalid order id");
        }
        var commissionOrder = commissionOrderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidCommissionOrderException("Commission order not found"))
                .fromEntity();

        commissionOrder.confirmBySeller(input.getUserId());

        commissionOrderRepository.save(CommissionOrderEntity.toEntity(commissionOrder));
    }
}
