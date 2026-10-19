package org.individualproject.semester3project.usecase.commission.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.usecase.commission.DeleteCommissionUseCase;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class DeleteCommissionUseCaseImpl implements DeleteCommissionUseCase {
    private final CommissionRepository commissionRepository;
    @Override
    public void deleteCommission(Long id) {
        commissionRepository.deleteById(id);
    }
}
