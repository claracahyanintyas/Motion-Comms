package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetActivePayoutMethodsByUserInput;

@Builder @Data
public class GetActivePayoutMethodsByUserRequest implements GetActivePayoutMethodsByUserInput{
    Long userId;
}
