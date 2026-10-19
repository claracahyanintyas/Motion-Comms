package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodInput;

@Builder @Data
@NoArgsConstructor @AllArgsConstructor
public class CreatePayoutMethodRequest implements CreatePayoutMethodInput {
    Long userId;

    PayoutMethodType type;

    @NotBlank
    String accountNumber;
    @NotBlank
    String providerName;
    @NotBlank
    String accountName;

}
