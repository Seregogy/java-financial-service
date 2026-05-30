package com.financial.loan.domain.domainexception.validation;

public class CarValidationException extends ValidationException {

    public CarValidationException(String field, String message) {
        super(field, message);
    }
}