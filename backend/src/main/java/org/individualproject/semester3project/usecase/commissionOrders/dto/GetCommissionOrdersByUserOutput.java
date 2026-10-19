package org.individualproject.semester3project.usecase.commissionOrders.dto;

import java.util.List;

public interface GetCommissionOrdersByUserOutput {
    List<CommissionOrderPublicData> getCommissionOrders();
}
