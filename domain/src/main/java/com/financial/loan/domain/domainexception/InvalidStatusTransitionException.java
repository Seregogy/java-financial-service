package com.financial.loan.domain.domainexception;

import com.financial.loan.domain.enums.Status;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(Status from, Status to) {
        super(String.format("Недопустимый переход по статусу из '%s' в '%s'", from, to));
    }

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
