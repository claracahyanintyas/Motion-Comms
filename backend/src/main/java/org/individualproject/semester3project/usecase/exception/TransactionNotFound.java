package org.individualproject.semester3project.usecase.exception;

public class TransactionNotFound extends RuntimeException {
    public TransactionNotFound() {
        super("Transaction not found");
    }
}
