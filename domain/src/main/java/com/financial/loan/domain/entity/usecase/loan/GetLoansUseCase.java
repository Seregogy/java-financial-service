package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.domainexception.InvalidPageException;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetLoansUseCase {

    private final LoanApplicationRepository loanRepository;

    public List<LoanApplication> execute(int page, int size) {

        if (page < 0) {
            throw new InvalidPageException("Номер страницы не может быть отрицательным");
        }
        if (size <= 0 || size > 100) {
            throw new InvalidPageException("Размер страницы должен быть от 1 до 100");
        }

        return loanRepository.getAll(page, size);
    }
}