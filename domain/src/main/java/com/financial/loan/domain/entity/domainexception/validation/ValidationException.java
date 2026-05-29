package com.financial.loan.domain.entity.domainexception.validation;

import com.financial.loan.domain.entity.domainexception.DomainException;

public class ValidationException extends DomainException {

    public ValidationException(String field, String message) {
        super("VALIDATION_" + field.toUpperCase(),
                String.format("Validation failed for field '%s': %s", field, message));
    }

    public ValidationException(String field, String message, Throwable cause) {
        super("VALIDATION_" + field.toUpperCase(),
                String.format("Validation failed for field '%s': %s", field, message), cause);
    }
}