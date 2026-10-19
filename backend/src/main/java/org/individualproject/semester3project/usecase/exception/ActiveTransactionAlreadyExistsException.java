package org.individualproject.semester3project.usecase.exception;

public class ActiveTransactionAlreadyExistsException extends RuntimeException {
    public ActiveTransactionAlreadyExistsException(String message) {
        super(message);
    }
}
