package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.usecase.payoutMethod.dto.UpdatePayoutMethodInput;

@Data @Builder @AllArgsConstructor
@NoArgsConstructor
public class UpdatePayoutMethodRequest implements UpdatePayoutMethodInput {

    PayoutMethodType type;
    String accountNumber;
    String providerName;
    String accountName;

}
