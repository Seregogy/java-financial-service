package com.financial.loan.domain.domainexception.validation;

public class UserValidationException extends ValidationException {

    public UserValidationException(String field, String message) {
        super(field, message);
    }
}