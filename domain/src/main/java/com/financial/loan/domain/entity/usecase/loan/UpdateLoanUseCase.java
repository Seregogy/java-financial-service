package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.domainexception.LoanNotFoundException;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.domainexception.LoanDeletionException;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.financial.loan.domain.entity.enums.Status.NEW;

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