package com.financial.loan.domain.entity;

import com.financial.loan.domain.entity.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    private final UUID id;
    private final String fullName;
    private final Role role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Result<User> create(
            String fullName,
            Role role) {

        Result<String> validFullName = validateFullName(fullName);
        if (validFullName.isFailure()) {
            return Result.failure(validFullName.getError());
        }

        Result<Role> validRole = validateRole(role);
        if (validRole.isFailure()) {
            return Result.failure(validRole.getError());
        }

        LocalDateTime now = LocalDateTime.now();

        return Result.success(new User(
                UUID.randomUUID(),
                fullName,
                role,
                now,
                now
        ));
    }

    private static Result<String> validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return Result.failure("fullName cannot be null or blank");
        }
        if (fullName.length() < 3) {
            return Result.failure("fullName must be at least 3 characters long");
        }
        if (fullName.length() > 100) {
            return Result.failure("fullName cannot exceed 100 characters");
        }
        return Result.success(fullName);
    }

    private static Result<Role> validateRole(Role role) {
        if (role == null) {
            return Result.failure("role cannot be null");
        }
        return Result.success(role);
    }
}