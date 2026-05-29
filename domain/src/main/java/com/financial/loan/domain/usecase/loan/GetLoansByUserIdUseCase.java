package com.financial.loan.domain.usecase.loan;

import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class GetLoansByUserIdUseCase {

    private final LoanApplicationRepository loanRepository;

    public List<LoanApplication> execute(UUID idUser) {

        if (idUser == null) {
            throw new UserNotFoundException("ID пользователя не может быть пустым");
        }

        List<LoanApplication> loans = loanRepository.getByIdUser(idUser);

        if (loans == null || loans.isEmpty()) {
            throw new UserNotFoundException(idUser);
        }

        return loans;
    }
}