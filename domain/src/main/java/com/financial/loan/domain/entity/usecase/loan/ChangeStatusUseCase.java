package com.financial.loan.domain.entity.usecase.loan;

import com.financial.loan.domain.entity.domainexception.InvalidStatusTransitionException;
import com.financial.loan.domain.entity.domainexception.LoanNotFoundException;
import com.financial.loan.domain.entity.entity.ApplicationHistory;
import com.financial.loan.domain.entity.entity.LoanApplication;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.ApplicationHistoryRepository;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.financial.loan.domain.entity.enums.Status.*;

@AllArgsConstructor
public class ChangeStatusUseCase {
    private final LoanApplicationRepository loanRepo;

    private final ApplicationHistoryRepository historyRepo;


    public UUID execute(UUID loanId, Status newStatus, UUID changedBy, String reason) {
        LoanApplication loan = loanRepo.getById(loanId);

        if (loan == null)
            throw new LoanNotFoundException("Заявки с " + loanId + "не существует");

        validateTransition(loan.getStatus(), newStatus);

        LoanApplication updated = loan.changeStatus(newStatus);
        loanRepo.update(loanId, updated);

        historyRepo.create(ApplicationHistory.create(
                loanId, loan.getStatus(),
                newStatus, changedBy,
                LocalDateTime.now()));

        return loanId;

    }


    private void validateTransition(Status from, Status to) {

        if (from == APPROVED || from == REJECTED || from == EXPIRED)
            throw new InvalidStatusTransitionException(from, to);

        if (from == NEW && to != IN_PROGRESS)
            throw new InvalidStatusTransitionException(from, to);
    }
}
