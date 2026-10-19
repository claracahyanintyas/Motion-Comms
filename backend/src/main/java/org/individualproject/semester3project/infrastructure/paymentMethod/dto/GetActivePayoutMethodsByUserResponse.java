package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetActivePayoutMethodsByUserOutput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;

import java.util.List;

@Data @Builder
public class GetActivePayoutMethodsByUserResponse implements GetActivePayoutMethodsByUserOutput {
    List<PayoutMethodPublicData> payoutMethods;
}
