package org.individualproject.semester3project.usecase.exception;

public class CommissionNotFoundException extends RuntimeException {
    public CommissionNotFoundException(String message) {
        super(message);
    }
    public CommissionNotFoundException(){
        super("Commission not found");
    }
}
