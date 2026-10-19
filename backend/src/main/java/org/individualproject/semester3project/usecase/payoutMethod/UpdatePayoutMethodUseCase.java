package org.individualproject.semester3project.usecase.payoutMethod;

import org.individualproject.semester3project.usecase.payoutMethod.dto.UpdatePayoutMethodInput;

public interface UpdatePayoutMethodUseCase {
    void updatePayoutMethod(Long payoutMethodId, UpdatePayoutMethodInput input);
}
