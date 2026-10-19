package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.GetCommissionOrderResponse;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.commissionOrders.GetCommissionOrderUseCase;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrderOutput;
import org.individualproject.semester3project.usecase.exception.CommissionOrderNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.mapper.CommissionOrderMapper;
import org.springframework.stereotype.Service;


@Service @AllArgsConstructor
public class GetCommissionOrderUseCaseImpl implements GetCommissionOrderUseCase {

    private CommissionOrderRepository commissionOrderRepository;

    @Override
    public GetCommissionOrderOutput getCommissionOrder(GetCommissionOrderInput input) {
        if (input == null || input.getId() == null || input.getId() < 1) {
            throw new InvalidInputException("Invalid id");
        }
        CommissionOrder commissionOrder = commissionOrderRepository.findById(input.getId())
                .orElseThrow(CommissionOrderNotFoundException::new)
                .fromEntity();

        var commissionPublicData = CommissionOrderMapper.ToCommissionPublicData(commissionOrder);

        return GetCommissionOrderResponse.builder()
                .commissionOrder(commissionPublicData).build();
    }
}
