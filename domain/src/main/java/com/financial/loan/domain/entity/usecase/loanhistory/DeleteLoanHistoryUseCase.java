package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
public class DeleteLoanHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Result<UUID> execute(UUID applicationHistoryId) {

        if (applicationHistoryId == null) {
            return Result.failure("ID записи истории не может быть null");
        }

        ApplicationHistory history = applicationHistoryRepository.getById(applicationHistoryId);
        if (history == null) {
            return Result.failure("Запись истории с ID " + applicationHistoryId + " не найдена");
        }

        try {
            UUID deletedId = applicationHistoryRepository.delete(applicationHistoryId);

            if (deletedId == null) {
                return Result.failure("Ошибка при удалении записи истории");
            }

            return Result.success(deletedId);
        } catch (Exception e) {
            return Result.failure("Ошибка удаления: " + e.getMessage());
        }
    }
}