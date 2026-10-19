package org.individualproject.semester3project.usecase.commission.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.infrastructure.commission.dto.GetAllCommissionsResponse;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.usecase.commission.GetAllCommissionsUseCase;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.commission.dto.GetAllCommissionsOutput;
import org.individualproject.semester3project.usecase.mapper.CommissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class GetAllCommissionsUseCaseImpl implements GetAllCommissionsUseCase {
    private final CommissionRepository commissionRepository;
    public GetAllCommissionsOutput getCommissions() {
        List<Commission> commissions = commissionRepository.findAll().stream().map(CommissionEntity::fromEntity).toList();
        List<CommissionPublicData> commissionPublicDataList = commissions.stream().map(CommissionMapper::ToCommissionPublicData).toList();
        return GetAllCommissionsResponse.builder()
                .commissions(commissionPublicDataList)
                .build();
    }
}
