package com.financial.loan.domain.domainexception;
import com.financial.loan.domain.enums.Status;

public class LoanDeletionException extends DomainException {

    public LoanDeletionException(String message) {
        super("LOAN_007", message);
    }

    public LoanDeletionException(Status status) {
        super("LOAN_007", String.format("Cannot delete loan application with status: %s", status));
    }
}