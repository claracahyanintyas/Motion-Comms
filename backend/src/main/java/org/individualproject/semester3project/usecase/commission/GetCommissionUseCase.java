package org.individualproject.semester3project.usecase.commission;

import org.individualproject.semester3project.usecase.commission.dto.GetCommissionInput;
import org.individualproject.semester3project.usecase.commission.dto.GetCommissionOutput;

public interface GetCommissionUseCase {
    GetCommissionOutput getCommission(GetCommissionInput input);
}
