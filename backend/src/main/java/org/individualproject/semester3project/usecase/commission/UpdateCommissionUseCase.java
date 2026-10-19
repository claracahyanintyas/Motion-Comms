package org.individualproject.semester3project.usecase.commission;

import org.individualproject.semester3project.usecase.commission.dto.UpdateCommissionInput;

public interface UpdateCommissionUseCase {
    void updateCommission(Long commissionId, UpdateCommissionInput updateCommissionInput);
}
