package org.individualproject.semester3project.usecase.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message) {
        super(message);
    }
    public EmailTakenException() {
        super("Email taken");
    }
}
