package org.individualproject.semester3project.usecase.exception;

public class CommissionOrderNotFoundException extends RuntimeException {
    public CommissionOrderNotFoundException(){
        super("Commission order not found");
    }
}
