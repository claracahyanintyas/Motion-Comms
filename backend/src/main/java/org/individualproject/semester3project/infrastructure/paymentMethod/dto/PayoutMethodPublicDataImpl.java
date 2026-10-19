package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.time.OffsetDateTime;

@Builder @Data
public class PayoutMethodPublicDataImpl implements PayoutMethodPublicData {
    Long id;
    UserPublicData user;
    PayoutMethodType type;
    String accountName;
    String accountNumber;
    String providerName;
    OffsetDateTime createdAt;

    @Builder.Default
    boolean active = true;

    OffsetDateTime deletedAt;
}
