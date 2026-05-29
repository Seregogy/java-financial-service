package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.domainexception.validation.ApplicationHistoryValidationException;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class UpdateLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public UUID execute(UUID applicationHistoryId,
                        UUID applicationId,
                        Status oldStatus,
                        Status newStatus,
                        UUID changedBy,
                        LocalDateTime changedAt) {

        if (applicationHistoryId == null) {
            throw new ApplicationHistoryValidationException("id", "ID записи истории не может быть null");
        }

        ApplicationHistory existingHistory = applicationHistoryRepository.getById(applicationHistoryId);
        if (existingHistory == null) {
            throw new ApplicationHistoryValidationException("id", "Запись истории с ID " + applicationHistoryId + " не найдена");
        }

        ApplicationHistory updatedHistory = ApplicationHistory.create(
                applicationId,
                oldStatus,
                newStatus,
                changedBy,
                changedAt
        );

        UUID updatedId = applicationHistoryRepository.update(applicationHistoryId, updatedHistory);

        if (updatedId == null) {
            throw new ApplicationHistoryValidationException("id", "Ошибка при обновлении записи истории");
        }

        return updatedId;
    }
}