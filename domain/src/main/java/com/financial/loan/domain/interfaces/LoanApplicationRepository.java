package com.financial.loan.domain.interfaces;

import com.financial.loan.domain.entity.LoanApplication;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository {
    public List<LoanApplication> getAll(int page, int size);

    public LoanApplication getById(UUID loanApplicationId);

    public UUID create(LoanApplication entity);

    public UUID update(
            UUID loanApplicationId,
            LoanApplication loanApplication
    );

    public List<LoanApplication> getByIdUser(UUID userId);

    public UUID delete(UUID loanApplicationId);
}
