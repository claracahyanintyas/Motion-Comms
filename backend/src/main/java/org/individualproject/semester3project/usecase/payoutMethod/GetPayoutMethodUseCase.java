package org.individualproject.semester3project.usecase.payoutMethod;

import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodInput;
import org.individualproject.semester3project.usecase.payoutMethod.dto.GetPayoutMethodOutput;

public interface GetPayoutMethodUseCase {
    GetPayoutMethodOutput getPayoutMethod(GetPayoutMethodInput input);
}
