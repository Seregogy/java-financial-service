package com.financial.loan.domain.entity.domainexception.validation;

public class LoanApplicationValidationException extends ValidationException {

    public LoanApplicationValidationException(String field, String message) {
        super(field, message);
    }
}