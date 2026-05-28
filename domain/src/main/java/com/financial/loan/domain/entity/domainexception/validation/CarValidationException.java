package com.financial.loan.domain.entity.domainexception.validation;

public class CarValidationException extends ValidationException {

    public CarValidationException(String field, String message) {
        super(field, message);
    }
}