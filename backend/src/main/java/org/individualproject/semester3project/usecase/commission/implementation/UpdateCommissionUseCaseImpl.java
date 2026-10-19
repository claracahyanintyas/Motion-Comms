package org.individualproject.semester3project.usecase.commission.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.usecase.commission.UpdateCommissionUseCase;
import org.individualproject.semester3project.usecase.commission.dto.UpdateCommissionInput;
import org.individualproject.semester3project.usecase.exception.CommissionNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class UpdateCommissionUseCaseImpl implements UpdateCommissionUseCase {
    private CommissionRepository commissionRepository;
    @Override
    public void updateCommission(Long commissionId, UpdateCommissionInput updateCommissionInput) {
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(CommissionNotFoundException::new)
                .fromEntity();

        if (updateCommissionInput.getDownPaymentPercentage().compareTo(BigDecimal.ZERO) < 0 || updateCommissionInput.getDownPaymentPercentage().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new InvalidInputException("Down payment percent must be between 0 and 100");
        }

        if (updateCommissionInput.getTitle() != null) {
            commission.changeTitle(updateCommissionInput.getTitle());
        }
        if (updateCommissionInput.getDescription() != null) {
            commission.changeDescription(updateCommissionInput.getDescription());
        }
        if (updateCommissionInput.getPrice() != null) {
            commission.changePrice(updateCommissionInput.getPrice());
        }
        if(updateCommissionInput.getDownPaymentPercentage() != null) {
            commission.changeDownPaymentPercentage(updateCommissionInput.getDownPaymentPercentage()
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        }
        commissionRepository.save(CommissionEntity.toEntity(commission));
    }
}
