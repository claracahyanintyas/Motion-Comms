package org.individualproject.semester3project.usecase.commissionOrders.dto;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;

public interface UpdateCommissionOrderStatusInput {
    CommissionOrderStatus getCommissionOrderStatus();
}
