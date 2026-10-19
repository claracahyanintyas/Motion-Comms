package org.individualproject.semester3project.usecase.payoutMethod.dto;

import java.util.List;

public interface GetActivePayoutMethodsByUserOutput {
    List<PayoutMethodPublicData> getPayoutMethods();
}
