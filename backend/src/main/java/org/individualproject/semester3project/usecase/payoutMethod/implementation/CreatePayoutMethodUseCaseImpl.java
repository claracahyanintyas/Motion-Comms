package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.CreatePayoutMethodResponse;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.InvalidUserException;
import org.individualproject.semester3project.usecase.payoutMethod.CreatePayoutMethodUseCase;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodInput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodOutput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class CreatePayoutMethodUseCaseImpl implements CreatePayoutMethodUseCase {
    private final UserRepository userRepository;
    private final PayoutMethodRepository payoutMethodRepository;
    @Override
    public CreatePayoutMethodOutput createPayoutMethod(CreatePayoutMethodInput input) {
        if  (input.getUserId() == null || input.getUserId() == 0L) {
            throw new InvalidUserException("User id cannot be null or empty");
        }
        if (input.getType() == null) {
            throw new InvalidInputException("PayoutMethodType cannot be null");
        }
        if (input.getAccountName() == null || input.getAccountName().isEmpty()) {
            throw new InvalidInputException("Account name cannot be null");
        }
        if (input.getAccountNumber() == null || input.getAccountNumber().isEmpty()) {
            throw new InvalidInputException("Account number cannot be null");
        }
        User user = userRepository.findById(input.getUserId())
                .orElseThrow(() -> new InvalidUserException("user not found"))
                .fromEntity();
        PayoutMethod payoutMethod = PayoutMethod.builder()
                .type(input.getType())
                .accountName(input.getAccountName())
                .accountNumber(input.getAccountNumber())
                .user(user)
                .providerName(input.getProviderName())
                .build();
        var response = payoutMethodRepository.save(PayoutMethodEntity.toEntity(payoutMethod));
        return CreatePayoutMethodResponse.builder()
                .id(response.getId()).build();
    }
}
