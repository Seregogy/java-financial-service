package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GetLoanHistoryByIdUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Result<ApplicationHistory> execute(UUID applicationHistoryId) {

        if (applicationHistoryId == null) {
            return Result.failure("ID записи истории не может быть null");
        }

        ApplicationHistory history = applicationHistoryRepository.getById(applicationHistoryId);

        if (history == null) {
            return Result.failure("Запись истории с ID " + applicationHistoryId + " не найдена");
        }

        return Result.success(history);
    }
}