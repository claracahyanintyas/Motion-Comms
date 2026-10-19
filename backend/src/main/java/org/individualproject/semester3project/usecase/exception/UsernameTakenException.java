package org.individualproject.semester3project.usecase.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String message) {
        super(message);
    }
    public UsernameTakenException() {
        super("Username taken");
    }
}
