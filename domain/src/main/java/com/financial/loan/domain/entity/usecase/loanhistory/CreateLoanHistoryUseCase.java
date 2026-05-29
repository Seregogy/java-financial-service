package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.domainexception.validation.ApplicationHistoryValidationException;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class CreateLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public UUID execute(
            UUID applicationId,
            Status oldStatus,
            Status newStatus,
            UUID changedBy,
            LocalDateTime changedAt) {

        ApplicationHistory history = ApplicationHistory.create(
                applicationId,
                oldStatus,
                newStatus,
                changedBy,
                changedAt
        );

        UUID createdId = applicationHistoryRepository.create(history);
        
        if (createdId == null) {
            throw new ApplicationHistoryValidationException("id", "Не удалось сохранить историю изменения заявки");
        }

        return createdId;
    }
}