package com.financial.loan.domain.domainexception.validation;

public class ApplicationHistoryValidationException extends ValidationException {

    public ApplicationHistoryValidationException(String field, String message) {
        super(field, message);
    }
}