package org.individualproject.semester3project.infrastructure.paymentMethod.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodOutput;

@Builder @Data
public class CreatePayoutMethodResponse implements CreatePayoutMethodOutput {

    Long id;
}
