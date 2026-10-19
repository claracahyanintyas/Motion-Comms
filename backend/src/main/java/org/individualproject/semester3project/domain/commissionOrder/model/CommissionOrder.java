package org.individualproject.semester3project.domain.commissionOrder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.exception.InvalidOrderStateException;
import org.individualproject.semester3project.domain.exception.UnauthorizedActionException;
import org.individualproject.semester3project.domain.user.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Builder @Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionOrder {
    private Long id;
    private Commission commission;
    private User buyer;
    private User seller;
    @Builder.Default
    private CommissionOrderStatus commissionOrderStatus = CommissionOrderStatus.PENDING_DOWN_PAYMENT;
    @Builder.Default
    boolean confirmedByBuyer = false;
    @Builder.Default
    boolean confirmedBySeller = false;
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    @Builder.Default
    boolean downPaymentPaid = false;
    @Builder.Default
    boolean fullPaymentPaid = false;
    private BigDecimal totalPrice;
    private BigDecimal downPaymentAmount;
    private BigDecimal remainingPaymentAmount;

    public CommissionOrder(Commission commission, User buyer)
    {
        this.commission = commission;
        this.buyer = buyer;
        this.seller = commission.getCommissionee();

        this.totalPrice = commission.getPrice();
        this.downPaymentAmount = totalPrice.multiply(commission.getDownPaymentPercentage())
                .setScale(2, RoundingMode.HALF_UP);
        this.remainingPaymentAmount = totalPrice.subtract(downPaymentAmount)
                .setScale(2, RoundingMode.HALF_UP);

        this.commissionOrderStatus = CommissionOrderStatus.PENDING_DOWN_PAYMENT;

        this.confirmedByBuyer = false;
        this.confirmedBySeller = false;

        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();

        this.downPaymentPaid = false;
        this.fullPaymentPaid = false;
    }

    public void confirmBySeller(Long sellerId) {
        if (!seller.getId().equals(sellerId)) {
            throw new UnauthorizedActionException("user is unauthorized");
        }
        this.confirmedBySeller = true;

        if (this.commissionOrderStatus != CommissionOrderStatus.IN_PROGRESS) {
            throw new InvalidOrderStateException("cannot send commission unless status is in progress");
        }
        if (fullPaymentPaid && !confirmedByBuyer) {
            this.commissionOrderStatus = CommissionOrderStatus.AWAITING_CONFIRMATION;
        } else if (confirmedByBuyer && fullPaymentPaid) {
            this.commissionOrderStatus = CommissionOrderStatus.COMPLETED;
        }
        else {
            this.commissionOrderStatus = CommissionOrderStatus.PENDING_REMAINING_PAYMENT;
        }
        this.updatedAt = OffsetDateTime.now();
    }

    public void confirmByBuyer(Long buyerId) {
        if (!buyer.getId().equals(buyerId)) {
            throw new UnauthorizedActionException("user is unauthorized");
        }
        if (this.commissionOrderStatus ==  CommissionOrderStatus.PENDING_DOWN_PAYMENT ||
        this.commissionOrderStatus ==  CommissionOrderStatus.COMPLETED ||
        this.commissionOrderStatus ==  CommissionOrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("cannot confirm received in current state");
        }
        this.confirmedByBuyer = true;

        if (confirmedBySeller && fullPaymentPaid) {
            this.commissionOrderStatus = CommissionOrderStatus.COMPLETED;
        }
        this.updatedAt = OffsetDateTime.now();
    }

    public void payDownPayment(){
        if (this.commissionOrderStatus == CommissionOrderStatus.PENDING_DOWN_PAYMENT) {
            this.downPaymentPaid = true;
            this.commissionOrderStatus = CommissionOrderStatus.IN_PROGRESS;
            this.updatedAt = OffsetDateTime.now();
        }
        else {
            throw new InvalidOrderStateException("cannot pay down payment unless status is in pending down payment");
        }
    }


    public void payRemaining(){
        if (this.commissionOrderStatus == CommissionOrderStatus.IN_PROGRESS ||
                this.commissionOrderStatus == CommissionOrderStatus.PENDING_REMAINING_PAYMENT){
            this.fullPaymentPaid = true;

            if (confirmedByBuyer && confirmedBySeller) {
                this.commissionOrderStatus = CommissionOrderStatus.COMPLETED;
            }
            else {
                if (commissionOrderStatus != CommissionOrderStatus.IN_PROGRESS) {
                    this.commissionOrderStatus = CommissionOrderStatus.AWAITING_CONFIRMATION;
                }

            }
            this.updatedAt = OffsetDateTime.now();
        }
        else {
            throw new InvalidOrderStateException("cannot pay remaining in current status");
        }

    }
}
