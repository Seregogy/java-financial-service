package com.financial.loan.domain.entity.usecase.loan;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public class GetLoanByIdUseCase {

    private final LoanApplicationRepository loanRepository;

    public Result<LoanApplication> execute(UUID idLoanApplication) {

        if (idLoanApplication == null ) {
            return Result.failure("ID заявки не может быть пустым или отрицательным");
        }

        LoanApplication loan = loanRepository.getById(idLoanApplication);

        if (loan == null) {
            return Result.failure("Заявка с ID " + idLoanApplication + " не найдена");
        }

        return Result.success(loan);
    }
}