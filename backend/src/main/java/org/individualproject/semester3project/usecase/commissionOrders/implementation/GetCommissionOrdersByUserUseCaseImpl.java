package org.individualproject.semester3project.usecase.commissionOrders.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.GetCommissionOrdersByUserResponse;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserOutput;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.mapper.CommissionOrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor @Service
public class GetCommissionOrdersByUserUseCaseImpl implements org.individualproject.semester3project.usecase.commissionOrders.GetCommissionOrdersByUserUseCase {

    private CommissionOrderRepository commissionOrderRepository;
    @Override
    public GetCommissionOrdersByUserOutput getCommissionOrdersByUser(GetCommissionOrdersByUserInput input) {
        if (input.getUserId() == null || input.getUserId() <= 0) {
            throw new InvalidInputException("User id cannot be empty or invalid");
        }

        List<CommissionOrderPublicData> orders;

        if (input.getRole() == null) {

            orders = commissionOrderRepository.findAllByUserId(input.getUserId())
                    .stream()
                    .map(CommissionOrderEntity::fromEntity)
                    .map(CommissionOrderMapper::ToCommissionPublicData)
                    .toList();
        }
        else{
            orders = commissionOrderRepository.findByUserRole(input.getUserId(), input.getRole())
                    .stream()
                    .map(CommissionOrderEntity::fromEntity)
                    .map(CommissionOrderMapper::ToCommissionPublicData)
                    .toList();
        }
        return GetCommissionOrdersByUserResponse.builder()
                .commissionOrders(orders).build();

    }
}
