package com.financial.loan.domain.domainexception;

import com.financial.loan.domain.ValueObject.Passport;

public class DuplicateLoanApplicationException extends DomainException {

    public DuplicateLoanApplicationException(Passport passportNumber) {
        super("LOAN_006", String.format("Duplicate loan application with passport %s within last 24 hours", passportNumber));
    }
}