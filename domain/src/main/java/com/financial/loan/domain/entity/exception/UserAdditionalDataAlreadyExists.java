package com.financial.loan.domain.entity.exception;

public class UserAdditionalDataAlreadyExists extends RuntimeException {
    public UserAdditionalDataAlreadyExists(String message) {
        super(message);
    }
}
