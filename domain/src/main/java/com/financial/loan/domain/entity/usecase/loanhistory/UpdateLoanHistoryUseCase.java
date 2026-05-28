package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class UpdateLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Result<UUID> execute(UUID applicationHistoryId,
                                UUID applicationId,
                                Status oldStatus,
                                Status newStatus,
                                UUID changedBy,
                                LocalDateTime changedAt) {


        if (applicationHistoryId == null) {
            return Result.failure("ID записи истории не может быть null");
        }

        ApplicationHistory existingHistory = applicationHistoryRepository.getById(applicationHistoryId);
        if (existingHistory == null) {
            return Result.failure("Запись истории с ID " + applicationHistoryId + " не найдена");
        }


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

        ApplicationHistory updatedHistory = historyResult.getValue();


        UUID updatedId = applicationHistoryRepository.update(applicationHistoryId, updatedHistory);

        if (updatedId == null) {
            return Result.failure("Ошибка при обновлении записи истории");
        }

        return Result.success(updatedId);
    }
}