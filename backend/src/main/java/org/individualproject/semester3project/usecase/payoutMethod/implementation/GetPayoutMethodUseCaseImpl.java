package org.individualproject.semester3project.usecase.payoutMethod.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.GetPayoutMethodResponse;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.PayoutMethodNotFound;
import org.individualproject.semester3project.usecase.mapper.PayoutMethodMapper;
import org.individualproject.semester3project.usecase.payoutMethod.GetPayoutMethodUseCase;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodInput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodOutput;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class GetPayoutMethodUseCaseImpl implements GetPayoutMethodUseCase {
    PayoutMethodRepository payoutMethodRepository;
    @Override
    public GetPayoutMethodOutput getPayoutMethod(GetPayoutMethodInput input) {
        if (input == null || input.getId() == null || input.getId() < 1) {
            throw new InvalidInputException("Id is invalid or null");
        }
        PayoutMethod method =  payoutMethodRepository.findById(input.getId()).orElseThrow(PayoutMethodNotFound::new).fromEntity();

        var methodPublicData = PayoutMethodMapper.toPayoutMethodPublicData(method);

        return GetPayoutMethodResponse.builder().payoutMethod(methodPublicData).build();
    }
}
