package org.individualproject.semester3project.usecase.payoutMethod.dto;

import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;

public interface CreatePayoutMethodInput {
    Long getUserId();
    PayoutMethodType getType();
    String getAccountName();
    String getAccountNumber();
    String getProviderName();
}
