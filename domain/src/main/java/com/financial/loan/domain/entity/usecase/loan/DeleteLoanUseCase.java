package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
public class DeleteLoanUseCase {

    private final LoanApplicationRepository loanRepository;

    public Result<UUID> execute(UUID idLoanApplication) {

        if (idLoanApplication == null ) {

            return Result.failure("ID заявки должен быть в формате UUID");
        }


        LoanApplication loan = loanRepository.getById(idLoanApplication);
        if (loan == null) {
            return Result.failure("Заявка с ID " + idLoanApplication + " не найдена");
        }


        String errorMessage = canDelete(loan.getStatus());
        if (errorMessage != null) {
            return Result.failure(errorMessage);
        }


        try {
            UUID deleted = loanRepository.delete(idLoanApplication);

            if (deleted==null) {
                return Result.failure("Ошибка при удалении заявки");
            }

            return Result.success();

        }
        catch (Exception e) {
            return Result.failure("Ошибка удаления: " + e.getMessage());
        }
    }


    private String canDelete(Status status) {
        switch (status) {
            case NEW:
                return null;

            case IN_PROGRESS:
                return "Нельзя удалить заявку в статусе 'В обработке' (IN_PROGRESS)";

            case APPROVED:
                return "Нельзя удалить одобренную заявку (APPROVED)";

            case REJECTED:
                return null;

            case EXPIRED:
                return null;

            default:
                return "Неизвестный статус заявки";
        }
    }
}