package com.financial.loan.domain.domainexception.validation;

public class LoanApplicationValidationException extends ValidationException {

    public LoanApplicationValidationException(String field, String message) {
        super(field, message);
    }
}