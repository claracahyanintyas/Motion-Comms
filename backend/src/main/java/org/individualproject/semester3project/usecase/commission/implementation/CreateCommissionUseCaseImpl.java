package org.individualproject.semester3project.usecase.commission.implementation;

import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.commission.dto.CreateCommissionResponse;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.springframework.stereotype.Service;
import org.individualproject.semester3project.usecase.commission.CreateCommissionUseCase;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionInput;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionOutput;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@Service

public class CreateCommissionUseCaseImpl implements CreateCommissionUseCase {
    private final CommissionRepository commissionRepository;
    private final UserRepository userRepository;
    private final PayoutMethodRepository payoutMethodRepository;
    @Override
    public CreateCommissionOutput createCommission(CreateCommissionInput input) {
        Long commissioneeId = input.getCommissioneeId();
        var dp = input.getDownPaymentPercentage();
        if (commissioneeId <= 0){
            throw new InvalidInputException("commissionee id is invalid");
        }
        if (input.getTitle().isEmpty()){
            throw new InvalidInputException("title cannot be empty");
        }
        if (dp == null) {
            throw new InvalidInputException("Down payment percent is required.");
        }
        if (dp.compareTo(BigDecimal.ZERO) < 0 && dp.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new InvalidInputException("Percent cannot be negative and be greater than 100.");
        }
        User commissionee = userRepository.findById(commissioneeId)
                .orElseThrow(() -> new InvalidUserException("commissionee not found"))
                .fromEntity();
        var payoutMethods = payoutMethodRepository.findByUserIdAndActiveTrue(commissionee.getId());
        if (payoutMethods.isEmpty()) {
            throw new InvalidUserException("Commissionee has no active payout method");
        }
        Commission newCommission = Commission.builder()
                .title(input.getTitle())
                .price(input.getPrice())
                .description(input.getDescription())
                .commissionee(commissionee)
                .downPaymentPercentage(input.getDownPaymentPercentage()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                .build();
        Commission returnedCommission = commissionRepository.save(CommissionEntity.toEntity(newCommission)).fromEntity();
        return CreateCommissionResponse
                .builder().id(returnedCommission.getId())
                .build();
    }
}
