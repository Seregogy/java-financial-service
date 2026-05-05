package com.financial.loan.domain.entity.interfaces;

import com.financial.loan.domain.entity.ApplicationHistory;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.entity.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository {
    public List<LoanApplication> getAll();

    public LoanApplication get(UUID loanApplicationId);

    public UUID create(LoanApplication entity);

    public UUID update(
            UUID loanApplicationId,
            UUID carId,
            UUID userId,
            BigDecimal loanAmount,
            BigDecimal firstPayment,
            LocalDateTime term,
            Status status
    );

    public UUID delete(UUID loanApplicationId);
}
