package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.GetActivePayoutMethodsByUserResponse;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.mapper.PayoutMethodMapper;
import org.individualproject.semester3project.usecase.payoutMethod.GetActivePayoutMethodsByUserUseCase;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetActivePayoutMethodsByUserInput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetActivePayoutMethodsByUserOutput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class GetActivePayoutMethodsByUserUseCaseImpl implements GetActivePayoutMethodsByUserUseCase {

    PayoutMethodRepository payoutMethodRepository;

    @Override
    public GetActivePayoutMethodsByUserOutput getActivePayoutMethodByUser(GetActivePayoutMethodsByUserInput input) {

        if (input == null || input.getUserId() == null || input.getUserId() < 1) {
            throw new InvalidInputException("Invalid user id");
        }
        var payoutMethods = payoutMethodRepository.findByUserIdAndActiveTrue(input.getUserId())
                .stream()
                .map(PayoutMethodEntity::fromEntity)
                .toList();

        var payoutMethodsPublicData = payoutMethods.stream().map(PayoutMethodMapper::toPayoutMethodPublicData).toList();

        return GetActivePayoutMethodsByUserResponse.builder()
                .payoutMethods(payoutMethodsPublicData)
                .build();

    }
}
