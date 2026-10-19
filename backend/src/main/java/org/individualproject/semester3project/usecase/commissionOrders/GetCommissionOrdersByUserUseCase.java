package org.individualproject.semester3project.usecase.commissionOrders;


import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserInput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserOutput;

public interface GetCommissionOrdersByUserUseCase {
    GetCommissionOrdersByUserOutput getCommissionOrdersByUser(GetCommissionOrdersByUserInput input);
}
