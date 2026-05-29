package com.financial.loan.domain.usecase.loan;

import com.financial.loan.domain.domainexception.LoanNotFoundException;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.domainexception.LoanDeletionException;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static com.financial.loan.domain.enums.Status.NEW;

@AllArgsConstructor
public class UpdateLoanUseCase {

    private final LoanApplicationRepository _loanRepository;

    public UUID execute(UUID idLoan, BigDecimal loanAmount, BigDecimal firstPayment) {
        LoanApplication existing = _loanRepository.getById(idLoan);

        if (existing == null)
            throw new LoanNotFoundException("Заявки с " + idLoan + "не существует");

        if (existing.getStatus() != NEW)
            throw new LoanDeletionException("Нельзя менять заявку в обработке");

        LoanApplication updated = existing.updateLoanAmount(loanAmount);

        _loanRepository.update(idLoan, updated);

        return idLoan;
    }
}