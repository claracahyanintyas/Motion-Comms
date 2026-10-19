package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodInput;

@Data @Builder
public class GetPayoutMethodRequest implements GetPayoutMethodInput {
    Long id;
}
