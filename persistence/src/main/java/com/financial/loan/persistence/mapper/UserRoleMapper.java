package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.enums.Role;
import com.financial.loan.persistence.model.enums.UserRole;

public class UserRoleMapper {
    public static Role toDomain(UserRole dbRole) {
        return switch (dbRole) {
            case USER -> Role.USER;
            case MODERATOR -> Role.MODERATOR;
            case ADMIN -> Role.ADMIN;
        };
    }

    public static UserRole toDb(Role domainRole) {
        return switch (domainRole) {
            case USER -> UserRole.USER;
            case MODERATOR -> UserRole.MODERATOR;
            case ADMIN -> UserRole.ADMIN;
        };
    }
}
