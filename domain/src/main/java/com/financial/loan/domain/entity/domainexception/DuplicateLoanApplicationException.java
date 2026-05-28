package com.financial.loan.domain.entity.domainexception;

public class DuplicateLoanApplicationException extends DomainException {

    public DuplicateLoanApplicationException(String passportNumber) {
        super("LOAN_006", String.format("Duplicate loan application with passport %s within last 24 hours", passportNumber));
    }
}