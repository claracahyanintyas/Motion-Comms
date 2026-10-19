package org.individualproject.semester3project.usecase.exception;

public class PayoutMethodNotFound extends RuntimeException {
    public PayoutMethodNotFound() {
        super("Payout method not found");
    }
}
