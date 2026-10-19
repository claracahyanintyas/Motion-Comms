package org.individualproject.semester3project.usecase.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
    public InvalidUserException() {
        super("Invalid User");
    }
}
