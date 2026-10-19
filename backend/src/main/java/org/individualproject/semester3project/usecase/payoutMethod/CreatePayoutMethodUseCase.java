package org.individualproject.semester3project.usecase.payoutMethod;

import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodInput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodOutput;

public interface CreatePayoutMethodUseCase {
    CreatePayoutMethodOutput createPayoutMethod(CreatePayoutMethodInput input);
}
