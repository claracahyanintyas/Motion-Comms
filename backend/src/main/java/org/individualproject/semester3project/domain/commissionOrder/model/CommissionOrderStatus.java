package org.individualproject.semester3project.domain.commissionOrder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public enum CommissionOrderStatus {
    PENDING_DOWN_PAYMENT("pending down payment"),
    IN_PROGRESS("in progress"),
    PENDING_REMAINING_PAYMENT("pending remaining payment"),
    AWAITING_CONFIRMATION("awaiting confirmation"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private String displayName;
}
