package org.individualproject.semester3project.usecase.commission;

import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionInput;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionOutput;

public interface CreateCommissionUseCase {
    CreateCommissionOutput createCommission(CreateCommissionInput createCommissionRequestModel);
}
