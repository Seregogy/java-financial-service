package com.financial.loan.domain.usecase.loan;

import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.domainexception.LoanDeletionException;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GetLoanByIdUseCase {

    private final LoanApplicationRepository loanRepository;

    public LoanApplication execute(UUID idLoanApplication) {

        if (idLoanApplication == null) {
            throw new LoanDeletionException("ID заявки не может быть пустым");
        }

        LoanApplication loan = loanRepository.getById(idLoanApplication);

        if (loan == null) {
            throw new LoanDeletionException("Заявка с ID " + idLoanApplication + " не найдена");
        }

        return loan;
    }
}