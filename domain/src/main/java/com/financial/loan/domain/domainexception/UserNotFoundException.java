package com.financial.loan.domain.domainexception;

import java.util.UUID;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String id) {
        super("USER_001", String.format("User with id %s not found", id));
    }

    public UserNotFoundException(UUID id) {
        this(id.toString());
    }
}