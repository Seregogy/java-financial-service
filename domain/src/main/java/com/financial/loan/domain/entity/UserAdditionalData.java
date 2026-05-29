package com.financial.loan.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAdditionalData {
    private final UUID id;
    private final UUID userId;
    private final LocalDateTime birthday;
    private final String password;
    private final BigDecimal monthlyIncome;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final String passport;

    public static Result<UserAdditionalData> create(
            UUID userId,
            LocalDateTime birthday,
            String password,
            BigDecimal monthlyIncome,
            String passport) {

        Result<UUID> validUserId = validateUserId(userId);
        if (validUserId.isFailure()) {
            return Result.failure(validUserId.getError());
        }

        Result<String> validPassword = validatePassword(password);
        if (validPassword.isFailure()) {
            return Result.failure(validPassword.getError());
        }

        Result<BigDecimal> validMonthlyIncome = validateMonthlyIncome(monthlyIncome);
        if (validMonthlyIncome.isFailure()) {
            return Result.failure(validMonthlyIncome.getError());
        }

        LocalDateTime now = LocalDateTime.now();

        return Result.success(new UserAdditionalData(
                UUID.randomUUID(),
                userId,
                birthday,
                password,
                monthlyIncome,
                now,
                now,
                passport
        ));
    }

    private static Result<UUID> validateUserId(UUID userId) {
        if (userId == null) {
            return Result.failure("userId cannot be null");
        }
        return Result.success(userId);
    }

    private static Result<String> validatePassword(String password) {
        if (password == null || password.isBlank()) {
            return Result.failure("password cannot be blank");
        }
        return Result.success(password);
    }

    private static Result<BigDecimal> validateMonthlyIncome(BigDecimal monthlyIncome) {
        if (monthlyIncome == null) {
            return Result.failure("monthlyIncome cannot be null");
        }
        if (monthlyIncome.compareTo(BigDecimal.ZERO) < 0) {
            return Result.failure("monthlyIncome cannot be negative");
        }
        return Result.success(monthlyIncome);
    }
}