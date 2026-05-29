package com.financial.loan.domain.entity.domainexception;

import java.util.UUID;

public class TooManyActiveLoansException extends DomainException {

    public TooManyActiveLoansException(UUID userId, int maxLoans) {
        super("LOAN_005", String.format("User %s has too many active loans (max: %d)", userId, maxLoans));
    }
}