package com.financial.loan.domain.entity.entity;


import com.financial.loan.domain.entity.domainexception.validation.UserAdditionalDataValidationException;
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

    public static UserAdditionalData create(
            UUID userId,
            LocalDateTime birthday,
            String password,
            BigDecimal monthlyIncome) {

        validateUserId(userId);
        validatePassword(password);
        validateMonthlyIncome(monthlyIncome);

        LocalDateTime now = LocalDateTime.now();

        return new UserAdditionalData(
                UUID.randomUUID(),
                userId,
                birthday,
                password,
                monthlyIncome,
                now,
                now
        );
    }

    public UserAdditionalData updatePassword(String password) {
        validatePassword(password);
        return new UserAdditionalData(
                this.id,
                this.userId,
                this.birthday,
                password,
                this.monthlyIncome,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public UserAdditionalData updateMonthlyIncome(BigDecimal monthlyIncome) {
        validateMonthlyIncome(monthlyIncome);
        return new UserAdditionalData(
                this.id,
                this.userId,
                this.birthday,
                this.password,
                monthlyIncome,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    private static void validateUserId(UUID userId) {
        if (userId == null) {
            throw new UserAdditionalDataValidationException("userId", "userId cannot be null");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new UserAdditionalDataValidationException("password", "password cannot be blank");
        }
        if (password.length() < 6) {
            throw new UserAdditionalDataValidationException("password", "password must be at least 6 characters long");
        }
    }

    private static void validateMonthlyIncome(BigDecimal monthlyIncome) {
        if (monthlyIncome == null) {
            throw new UserAdditionalDataValidationException("monthlyIncome", "monthlyIncome cannot be null");
        }
        if (monthlyIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new UserAdditionalDataValidationException("monthlyIncome", "monthlyIncome cannot be negative");
        }
    }
}