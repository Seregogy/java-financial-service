package com.financial.loan.domain.entity.domainexception;

public class InvalidPageException extends DomainException {

    public InvalidPageException(String message) {
        super("PAGE_001", message);
    }
}