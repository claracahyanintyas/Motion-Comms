package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodOutput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.PayoutMethodPublicData;

@Data
@Builder
public class GetPayoutMethodResponse implements GetPayoutMethodOutput {
    PayoutMethodPublicData payoutMethod;
}
