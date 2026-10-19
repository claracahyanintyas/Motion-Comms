package org.individualproject.semester3project.usecase.mapper;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.CommissionOrderPublicDataImpl;
import org.individualproject.semester3project.usecase.commission.dto.CommissionPublicData;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CommissionOrderPublicData;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

public class CommissionOrderMapper {
    public static CommissionOrderPublicData ToCommissionPublicData(CommissionOrder order){
        UserPublicData sellerPublicData = UserMapper.mapUserToUserPublicData(order.getSeller());
        UserPublicData buyerPublicData = UserMapper.mapUserToUserPublicData(order.getBuyer());
        CommissionPublicData commissionPublicData = CommissionMapper.ToCommissionPublicData(order.getCommission());

        return CommissionOrderPublicDataImpl.builder()
                .id(order.getId())
                .seller(sellerPublicData)
                .buyer(buyerPublicData)
                .commission(commissionPublicData)
                .status(order.getCommissionOrderStatus().name())
                .confirmedByBuyer(order.isConfirmedByBuyer())
                .confirmedBySeller(order.isConfirmedBySeller())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalPrice(order.getTotalPrice())
                .downPaymentAmount(order.getDownPaymentAmount())
                .remainingPaymentAmount(order.getRemainingPaymentAmount())
                .downPaymentPaid(order.isDownPaymentPaid())
                .fullPaymentPaid(order.isFullPaymentPaid())
                .build();
    }
}
