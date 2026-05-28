
package com.financial.loan.domain.entity.usecase.loan;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class GetLoansByUserIdUseCase {

    private final LoanApplicationRepository loanRepository;

    public Result<List<LoanApplication>> execute(UUID idUser) {

        if (idUser == null ) {
            return Result.failure("ID заявки не может быть пустым или отрицательным");
        }

        List<LoanApplication> loan = loanRepository.getByIdUser(idUser);

        if (loan == null) {
            return Result.failure("Заявка с ID " + idUser + " не найдена");
        }


        return Result.success(loan);
    }
}