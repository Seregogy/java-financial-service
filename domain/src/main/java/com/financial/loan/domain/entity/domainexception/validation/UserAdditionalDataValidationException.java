package com.financial.loan.domain.entity.domainexception.validation;

public class UserAdditionalDataValidationException extends ValidationException {

    public UserAdditionalDataValidationException(String field, String message) {
        super(field, message);
    }
}