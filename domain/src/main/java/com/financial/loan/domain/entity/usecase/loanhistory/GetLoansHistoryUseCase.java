package com.financial.loan.domain.entity.usecase.loanhistory;

import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetLoansHistoryUseCase {

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public Result<List<ApplicationHistory>> execute(int page, int size) {

        if (page < 0) {
            return Result.failure("Номер страницы не может быть отрицательным");
        }
        if (size <= 0 || size > 100) {
            return Result.failure("Размер страницы должен быть от 1 до 100");
        }

        List<ApplicationHistory> histories = applicationHistoryRepository.getAll(page, size);

        if (histories == null) {
            return Result.failure("Ошибка получения списка истории");
        }

        return Result.success(histories);
    }
}