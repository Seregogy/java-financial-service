package com.financial.loan.domain.enums;

public enum Role {
    USER,
    MODERATOR,
    ADMIN;

    public Boolean require(Role role) {
        return this == role;
    }
}
