package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.individualproject.semester3project.usecase.payoutMethod.SoftDeletePayoutMethodUseCase;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class SoftDeletePayoutMethodUseCaseImpl implements SoftDeletePayoutMethodUseCase {
    PayoutMethodRepository payoutMethodRepository;
    @Override
    public void softDeletePayoutMethod(Long id) {
        if (id == null || id < 1) {
            throw new InvalidInputException("Id cannot be null or less than 1");
        }
        var payoutMethod = payoutMethodRepository.findById(id).orElseThrow(PayoutMethodNotFound::new).fromEntity();
        payoutMethod.deactivate();
        payoutMethodRepository.save(PayoutMethodEntity.toEntity(payoutMethod));
    }
}
