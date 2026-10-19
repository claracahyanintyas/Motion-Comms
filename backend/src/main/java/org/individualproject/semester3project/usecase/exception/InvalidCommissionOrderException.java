package org.individualproject.semester3project.usecase.exception;

public class InvalidCommissionOrderException extends RuntimeException {
    public InvalidCommissionOrderException(String message) {
        super(message);
    }
}
