package com.financial.loan.domain.usecase.loan;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.enums.Status;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
public class IsActiveLoanUseCase
{
    private static final long MAX_ACTIVE_HOURS = 72;
    private final LoanApplicationRepository loanRepository;

    public boolean execute(UUID id)
    {

        LoanApplication loan = loanRepository.getById(id);
        if (loan == null) {
            return false;
        }

        Status currentStatus = loan.getStatus();
        if (currentStatus != Status.NEW && currentStatus != Status.IN_PROGRESS) {
            return false;
        }

        LocalDateTime startTime  = loan.getCreated();
        if (startTime == null) {
            return false;
        }


        Duration duration = Duration.between(startTime, LocalDateTime.now());
        boolean isActive = duration.toHours() <= MAX_ACTIVE_HOURS;


        // if (!isActive && currentStatus != Status.EXPIRED) {
        // loanRepository.markAsExpired(loan);
        // }

        return isActive;
    }
}
