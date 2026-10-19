package org.individualproject.semester3project.usecase.mapper;

import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethod;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.PayoutMethodPublicDataImpl;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

public class PayoutMethodMapper {
    public static PayoutMethodPublicData toPayoutMethodPublicData(PayoutMethod payoutMethod) {
        UserPublicData user = UserMapper.mapUserToUserPublicData(payoutMethod.getUser());

        return PayoutMethodPublicDataImpl.builder()
                .id(payoutMethod.getId())
                .type(payoutMethod.getType())
                .accountName(payoutMethod.getAccountName())
                .accountNumber(payoutMethod.getAccountNumber())
                .providerName(payoutMethod.getProviderName())
                .user(user)
                .active(payoutMethod.isActive())
                .deletedAt(payoutMethod.getDeletedAt())
                .createdAt(payoutMethod.getCreatedAt())
                .build();
    }
}
