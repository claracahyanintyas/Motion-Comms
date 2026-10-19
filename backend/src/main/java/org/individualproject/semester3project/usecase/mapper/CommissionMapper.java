package org.individualproject.semester3project.usecase.mapper;

import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.infrastructure.commission.dto.CommissionPublicDataImpl;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

public class CommissionMapper {
    public static CommissionPublicData ToCommissionPublicData(Commission commission){
        UserPublicData commissioneePublicData = UserMapper.mapUserToUserPublicData(commission.getCommissionee());

        return CommissionPublicDataImpl.builder()
                .id(commission.getId())
                .title(commission.getTitle())
                .description(commission.getDescription())
                .price(commission.getPrice())
                .commissionee(commissioneePublicData)
                .downPaymentPercentage(commission.getDownPaymentPercentage())
                .build();
    }
}
