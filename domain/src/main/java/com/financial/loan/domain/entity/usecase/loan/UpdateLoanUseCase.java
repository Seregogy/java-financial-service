package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.domainexception.LoanDeletionException;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class UpdateLoanUseCase {

    private final LoanApplicationRepository _loanRepository;

    public UUID execute(UUID idLoan, UUID carId, UUID userId, BigDecimal loanAmount, BigDecimal firstPayment, LocalDateTime term) {
        
        if (idLoan == null) {
            throw new LoanDeletionException("ID обновляемой заявки не может быть null");
        }

        LoanApplication loan = LoanApplication.create(carId, userId, loanAmount, firstPayment, term);

        _loanRepository.update(idLoan, loan);

        return loan.getId();
    }
}