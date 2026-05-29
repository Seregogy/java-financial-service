package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.domainexception.LoanDeletionException;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeleteLoanUseCase {

    private final LoanApplicationRepository loanRepository;

    public UUID execute(UUID idLoanApplication) {

        if (idLoanApplication == null) {
            throw new LoanDeletionException("ID заявки должен быть в формате UUID");
        }

        LoanApplication loan = loanRepository.getById(idLoanApplication);
        if (loan == null) {
            throw new LoanDeletionException("Заявка с ID " + idLoanApplication + " не найдена");
        }

        validateCanDelete(loan.getStatus());

        UUID deleted = loanRepository.delete(idLoanApplication);
        if (deleted == null) {
            throw new LoanDeletionException("Ошибка при удалении заявки");
        }

        return deleted;
    }

    private void validateCanDelete(Status status) {
        switch (status) {
            case NEW:
            case REJECTED:
            case EXPIRED:
                break;

            case IN_PROGRESS:
            case APPROVED:
                throw new LoanDeletionException(status);

            default:
                throw new LoanDeletionException("Неизвестный статус заявки");
        }
    }
}