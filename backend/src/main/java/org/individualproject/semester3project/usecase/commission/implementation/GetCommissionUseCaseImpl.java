package org.individualproject.semester3project.usecase.commission.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.infrastructure.commission.dto.GetCommissionResponse;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.usecase.commission.GetCommissionUseCase;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionInput;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionOutput;
import org.individualproject.semester3project.usecase.exception.CommissionNotFoundException;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.mapper.CommissionMapper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GetCommissionUseCaseImpl implements GetCommissionUseCase {
    private final CommissionRepository commissionRepository;
    public GetCommissionOutput getCommission(GetCommissionInput input) {
        Long id = input.getId();
        if (id < 1)
        {throw new InvalidInputException("Commission id is invalid");}
        else {
            Commission commission = commissionRepository.findById(id)
                    .orElseThrow(CommissionNotFoundException::new)
                    .fromEntity();
            var commissionPublicData = CommissionMapper.ToCommissionPublicData(commission);
            return GetCommissionResponse.builder()
                    .commission(commissionPublicData)
                    .build();
        }
    }
}
