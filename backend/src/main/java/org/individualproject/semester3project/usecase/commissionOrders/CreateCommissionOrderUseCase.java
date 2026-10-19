package org.individualproject.semester3project.usecase.commissionOrders;

import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderOutput;

public interface CreateCommissionOrderUseCase {
    CreateCommissionOrderOutput createCommissionOrder(CreateCommissionOrderInput createCommissionOrderInput );
}
