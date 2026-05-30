package com.financial.loan.domain.usecase.loanhistory;

import com.financial.loan.domain.entity.ApplicationHistory;
import com.financial.loan.domain.domainexception.validation.ApplicationHistoryValidationException;
import com.financial.loan.domain.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GetLoanHistoryByIdUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public ApplicationHistory execute(UUID applicationHistoryId) {

        if (applicationHistoryId == null) {
            throw new ApplicationHistoryValidationException("id", "ID записи истории не может быть null");
        }

        ApplicationHistory history = applicationHistoryRepository.getById(applicationHistoryId);

        if (history == null) {
            throw new ApplicationHistoryValidationException("id", "Запись истории с ID " + applicationHistoryId + " не найдена");
        }

        return history;
    }
}