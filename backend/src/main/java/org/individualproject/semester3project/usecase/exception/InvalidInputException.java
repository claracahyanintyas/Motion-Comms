package org.individualproject.semester3project.usecase.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
    public InvalidInputException() {
        super("Invalid Input");
    }
}
