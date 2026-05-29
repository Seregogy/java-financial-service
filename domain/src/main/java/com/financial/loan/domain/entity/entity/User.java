package com.financial.loan.domain.entity.entity;

import com.financial.loan.domain.entity.domainexception.validation.UserValidationException;
import com.financial.loan.domain.entity.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private final UUID id;
    private final String fullName;
    private final Role role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static User create(String fullName, Role role) {
        validateFullName(fullName);
        validateRole(role);

        LocalDateTime now = LocalDateTime.now();

        return new User(
                UUID.randomUUID(),
                fullName,
                role,
                now,
                now
        );
    }

    public User updateFullName(String fullName) {
        validateFullName(fullName);
        return new User(
                this.id,
                fullName,
                this.role,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public User updateRole(Role role) {
        validateRole(role);
        return new User(
                this.id,
                this.fullName,
                role,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    private static void validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new UserValidationException("fullName", "fullName cannot be null or blank");
        }
        if (fullName.length() < 3) {
            throw new UserValidationException("fullName", "fullName must be at least 3 characters long");
        }
        if (fullName.length() > 100) {
            throw new UserValidationException("fullName", "fullName cannot exceed 100 characters");
        }
    }

    private static void validateRole(Role role) {
        if (role == null) {
            throw new UserValidationException("role", "role cannot be null");
        }
    }
}