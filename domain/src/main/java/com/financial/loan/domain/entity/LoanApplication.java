package com.financial.loan.domain.entity;

import com.financial.loan.domain.entity.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanApplication {
    private final UUID id;
    private final UUID carId;
    private final UUID userId;
    private final BigDecimal loanAmount;
    private final BigDecimal firstPayment;
    private final LocalDateTime term;
    private final Status status;
    private final LocalDateTime created;
    private final LocalDateTime updated;

    public static Result<LoanApplication> create(
            UUID carId,
            UUID userId,
            BigDecimal loanAmount,
            BigDecimal firstPayment,
            LocalDateTime term) {

        Result<UUID> validCarId = validateCarId(carId);
        if (validCarId.isFailure()) {
            return Result.failure(validCarId.getError());
        }

        Result<UUID> validUserId = validateUserId(userId);
        if (validUserId.isFailure()) {
            return Result.failure(validUserId.getError());
        }

        Result<BigDecimal> validLoanAmount = validateLoanAmount(loanAmount);
        if (validLoanAmount.isFailure()) {
            return Result.failure(validLoanAmount.getError());
        }

        Result<BigDecimal> validFirstPayment = validateFirstPayment(firstPayment, loanAmount);
        if (validFirstPayment.isFailure()) {
            return Result.failure(validFirstPayment.getError());
        }

        Result<LocalDateTime> validTerm = validateTerm(term);
        if (validTerm.isFailure()) {
            return Result.failure(validTerm.getError());
        }

        LocalDateTime now = LocalDateTime.now();

        return Result.success(new LoanApplication(
                UUID.randomUUID(),
                carId,
                userId,
                loanAmount,
                firstPayment,
                term,
                Status.NEW,
                now,
                now
        ));
    }

    private static Result<UUID> validateCarId(UUID carId) {
        if (carId == null) {
            return Result.failure("carId cannot be null");
        }
        return Result.success(carId);
    }

    private static Result<UUID> validateUserId(UUID userId) {
        if (userId == null) {
            return Result.failure("userId cannot be null");
        }
        return Result.success(userId);
    }

    private static Result<BigDecimal> validateLoanAmount(BigDecimal loanAmount) {
        if (loanAmount == null) {
            return Result.failure("loanAmount cannot be null");
        }
        if (loanAmount.compareTo(BigDecimal.ZERO) < 0) {
            return Result.failure("loanAmount cannot be negative");
        }
        if (loanAmount.compareTo(BigDecimal.ZERO) == 0) {
            return Result.failure("loanAmount must be greater than zero");
        }
        return Result.success(loanAmount);
    }

    private static Result<BigDecimal> validateFirstPayment(BigDecimal firstPayment, BigDecimal loanAmount) {
        if (firstPayment == null) {
            return Result.failure("firstPayment cannot be null");
        }
        if (firstPayment.compareTo(BigDecimal.ZERO) < 0) {
            return Result.failure("firstPayment cannot be negative");
        }
        if (firstPayment.compareTo(loanAmount) > 0) {
            return Result.failure("firstPayment cannot be greater than loanAmount");
        }
        return Result.success(firstPayment);
    }

    private static Result<LocalDateTime> validateTerm(LocalDateTime term) {
        if (term == null) {
            return Result.failure("term cannot be null");
        }
        if (term.isBefore(LocalDateTime.now())) {
            return Result.failure("term cannot be in the past");
        }
        return Result.success(term);
    }
}