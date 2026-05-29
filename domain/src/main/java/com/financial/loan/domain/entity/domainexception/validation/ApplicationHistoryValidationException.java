package com.financial.loan.domain.entity.domainexception.validation;

public class ApplicationHistoryValidationException extends ValidationException {

    public ApplicationHistoryValidationException(String field, String message) {
        super(field, message);
    }
}