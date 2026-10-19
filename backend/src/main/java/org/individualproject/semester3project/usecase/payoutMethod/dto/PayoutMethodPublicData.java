package org.individualproject.semester3project.usecase.payoutMethod.dto;

import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

import java.time.OffsetDateTime;

public interface PayoutMethodPublicData {

    Long getId();
    PayoutMethodType getType();
    UserPublicData getUser();
    OffsetDateTime getCreatedAt();
    String getAccountName();
    String getAccountNumber();
    String getProviderName();
    boolean isActive();
    OffsetDateTime getDeletedAt();
}
