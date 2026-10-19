package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.individualproject.semester3project.usecase.payoutMethod.UpdatePayoutMethodUseCase;
import org.individualproject.semester3project.usecase.payoutMethod.dto.UpdatePayoutMethodInput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class UpdatePayoutMethodUseCaseImpl implements UpdatePayoutMethodUseCase {
    private PayoutMethodRepository payoutMethodRepository;

    @Override
    public void updatePayoutMethod(Long payoutMethodId, UpdatePayoutMethodInput input) {
        if (payoutMethodId == null || payoutMethodId <= 0) {
            throw new InvalidInputException("Payout method id cannot be null or less than 1");
        }
        var method =  payoutMethodRepository.findById(payoutMethodId).orElseThrow(PayoutMethodNotFound::new).fromEntity();
        if (input.getAccountName() != null && !input.getAccountName().isEmpty()) {
            method.changeAccountName(input.getAccountName());
        }
        if (input.getAccountNumber() != null && !input.getAccountNumber().isEmpty()) {
            method.changeAccountNumber(input.getAccountNumber());
        }
        if (input.getProviderName() != null && !input.getProviderName().isEmpty()) {
            method.changeProviderName(input.getProviderName());
        }
        if (input.getType() != null)
        {
            method.changeType(input.getType());
        }
        payoutMethodRepository.save(PayoutMethodEntity.toEntity(method));
    }
}
