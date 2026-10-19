package org.individualproject.semester3project.usecase.exception;

public class InvalidCommissionException extends RuntimeException {
    public InvalidCommissionException(String message) {
        super(message);
    }
    public InvalidCommissionException() {
        super("Invalid Commission");
    }
}
