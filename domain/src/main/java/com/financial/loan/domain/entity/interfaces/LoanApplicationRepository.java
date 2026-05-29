package com.financial.loan.domain.entity.interfaces;
import com.financial.loan.domain.entity.entity.LoanApplication;
import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository {
    List<LoanApplication> getAll(int page, int size);

    LoanApplication getById(UUID loanApplicationId);

    List<LoanApplication> getByIdUser(UUID userId);

    UUID create(LoanApplication entity);

    UUID update(UUID idLoan , LoanApplication loan);

    UUID delete(UUID loanApplicationId);
}
