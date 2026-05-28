package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class CreateLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Result<UUID> execute(
            UUID applicationId,
            Status oldStatus,
            Status newStatus,
            UUID changedBy,
            LocalDateTime changedAt) {

        Result<ApplicationHistory> historyResult = ApplicationHistory.create(
                applicationId,
                oldStatus,
                newStatus,
                changedBy,
                changedAt
        );

        if (historyResult.isFailure()) {
            return Result.failure(historyResult.getError());
        }

        ApplicationHistory history = historyResult.getValue();
        UUID createdId = applicationHistoryRepository.create(history);

        return Result.success(createdId);
    }
}