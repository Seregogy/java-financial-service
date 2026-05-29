package com.financial.loan.domain.usecase.loan;

import com.financial.loan.domain.domainexception.InvalidStatusTransitionException;
import com.financial.loan.domain.domainexception.LoanNotFoundException;
import com.financial.loan.domain.entity.ApplicationHistory;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.enums.Status;
import com.financial.loan.domain.interfaces.ApplicationHistoryRepository;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.financial.loan.domain.enums.Status.*;

@AllArgsConstructor
public class ChangeStatusUseCase {
    private final LoanApplicationRepository loanRepo;
    private final ApplicationHistoryRepository historyRepo;


    public UUID execute(UUID loanId, Status newStatus, UUID changedBy, String reason)
        throws InvalidStatusTransitionException, LoanNotFoundException {
        LoanApplication loan = loanRepo.getById(loanId);

        if (loan == null)
            throw new LoanNotFoundException("Заявки с " + loanId + "не существует");

        validateTransition(loan.getStatus(), newStatus);

        LoanApplication updated = loan.changeStatus(newStatus);
        loanRepo.update(loanId, updated);

        historyRepo.create(ApplicationHistory.create(
                loanId, loan.getStatus(),
                newStatus, changedBy,
                LocalDateTime.now())
        );

        return loanId;

    }


    private void validateTransition(Status from, Status to)
        throws InvalidStatusTransitionException {

        if (from == APPROVED || from == REJECTED || from == EXPIRED)
            throw new InvalidStatusTransitionException(from, to);

        if (from == NEW && to != IN_PROGRESS)
            throw new InvalidStatusTransitionException(from, to);
    }
}
