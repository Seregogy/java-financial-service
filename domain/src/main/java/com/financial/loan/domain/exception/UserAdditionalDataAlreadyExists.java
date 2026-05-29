package com.financial.loan.domain.exception;

public class UserAdditionalDataAlreadyExists extends RuntimeException {
    public UserAdditionalDataAlreadyExists(String message) {
        super(message);
    }
}
