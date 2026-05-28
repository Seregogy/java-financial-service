package com.financial.loan.domain.entity.usecase.loan;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class GetLoansUseCase {

    private final LoanApplicationRepository loanRepository;

    public Result<List<LoanApplication>> execute(int page, int size) {

        if (page < 0) {
            return Result.failure("Номер страницы не может быть отрицательным");
        }
        if (size <= 0 || size > 100) {
            return Result.failure("Размер страницы должен быть от 1 до 100");
        }

        List<LoanApplication> loans = loanRepository.getAll(page, size);

        return Result.success(loans);
    }
}
