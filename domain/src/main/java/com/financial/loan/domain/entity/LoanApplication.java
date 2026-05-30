package com.financial.loan.domain.entity;

import com.financial.loan.domain.domainexception.validation.LoanApplicationValidationException;
import com.financial.loan.domain.enums.Status;
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

    public static LoanApplication create(
            UUID carId,
            UUID userId,
            BigDecimal loanAmount,
            BigDecimal firstPayment,
            LocalDateTime term) {

        validateCarId(carId);
        validateUserId(userId);
        validateLoanAmount(loanAmount);
        validateFirstPayment(firstPayment, loanAmount);
        validateTerm(term);

        LocalDateTime now = LocalDateTime.now();

        return new LoanApplication(
                UUID.randomUUID(),
                carId,
                userId,
                loanAmount,
                firstPayment,
                term,
                Status.NEW,
                now,
                now
        );
    }

    public LoanApplication changeStatus(Status newStatus) {
        if (newStatus == null) {
            throw new LoanApplicationValidationException("status", "new status cannot be null");
        }
        if (this.status == newStatus) {
            throw new LoanApplicationValidationException("status", "new status must be different from current status");
        }

        return new LoanApplication(
                this.id,
                this.carId,
                this.userId,
                this.loanAmount,
                this.firstPayment,
                this.term,
                newStatus,
                this.created,
                LocalDateTime.now()
        );
    }

    public LoanApplication updateLoanAmount(BigDecimal loanAmount) {
        validateLoanAmount(loanAmount);
        validateFirstPayment(this.firstPayment, loanAmount);

        return new LoanApplication(
                this.id,
                this.carId,
                this.userId,
                loanAmount,
                this.firstPayment,
                this.term,
                this.status,
                this.created,
                LocalDateTime.now()
        );
    }

    public boolean isActive() {
        return this.status == Status.NEW || this.status == Status.IN_PROGRESS;
    }

    public boolean isExpired() {
        return this.status == Status.EXPIRED;
    }

    public boolean isFinalStatus() {
        return this.status == Status.APPROVED ||
                this.status == Status.REJECTED ||
                this.status == Status.EXPIRED;
    }

    private static void validateCarId(UUID carId) {
        if (carId == null) {
            throw new LoanApplicationValidationException("carId", "carId cannot be null");
        }
    }

    private static void validateUserId(UUID userId) {
        if (userId == null) {
            throw new LoanApplicationValidationException("userId", "userId cannot be null");
        }
    }

    private static void validateLoanAmount(BigDecimal loanAmount) {
        if (loanAmount == null) {
            throw new LoanApplicationValidationException("loanAmount", "loanAmount cannot be null");
        }
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LoanApplicationValidationException("loanAmount", "loanAmount must be greater than zero");
        }
    }

    private static void validateFirstPayment(BigDecimal firstPayment, BigDecimal loanAmount) {
        if (firstPayment == null) {
            throw new LoanApplicationValidationException("firstPayment", "firstPayment cannot be null");
        }
        if (firstPayment.compareTo(BigDecimal.ZERO) < 0) {
            throw new LoanApplicationValidationException("firstPayment", "firstPayment cannot be negative");
        }
        if (firstPayment.compareTo(loanAmount) > 0) {
            throw new LoanApplicationValidationException("firstPayment", "firstPayment cannot be greater than loanAmount");
        }
    }

    private static void validateTerm(LocalDateTime term) {
        if (term == null) {
            throw new LoanApplicationValidationException("term", "term cannot be null");
        }
        if (term.isBefore(LocalDateTime.now())) {
            throw new LoanApplicationValidationException("term", "term cannot be in the past");
        }
    }
}