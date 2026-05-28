package com.financial.loan.domain.entity.usecase.loan;


import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class UpdateLoanUseCase {

    private final LoanApplicationRepository _loanRepository;

    public Result<UUID> execute(UUID idLoan ,UUID carId, UUID userId, BigDecimal loanAmount, BigDecimal firstPayment,  LocalDateTime term) {
        Result<LoanApplication> loanResult = LoanApplication.create(carId, userId, loanAmount, firstPayment , term);

        if (loanResult.isFailure()) {
            return  Result.failure(loanResult.getError());
        }

        LoanApplication loan = loanResult.getValue();

        _loanRepository.update(idLoan , loan);

        return Result.success(loan.getId());
    }
}
