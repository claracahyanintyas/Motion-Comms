package org.individualproject.semester3project.usecase.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super("User not found");
    }
}
