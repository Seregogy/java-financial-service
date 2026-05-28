package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.domainexception.validation.ApplicationHistoryValidationException;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeleteLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public UUID execute(UUID applicationHistoryId) {

        if (applicationHistoryId == null) {
            throw new ApplicationHistoryValidationException("id", "ID записи истории не может быть null");
        }

        ApplicationHistory history = applicationHistoryRepository.getById(applicationHistoryId);
        if (history == null) {
            throw new ApplicationHistoryValidationException("id", "Запись истории с ID " + applicationHistoryId + " не найдена");
        }

        UUID deletedId = applicationHistoryRepository.delete(applicationHistoryId);

        if (deletedId == null) {
            throw new ApplicationHistoryValidationException("id", "Ошибка при удалении записи истории");
        }

        return deletedId;
    }
}