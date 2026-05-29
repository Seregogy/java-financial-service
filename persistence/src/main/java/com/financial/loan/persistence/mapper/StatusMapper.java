package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.enums.Status;
import com.financial.loan.persistence.model.enums.LoanApplicationStatus;

public class StatusMapper {
    public Status toDomain(LoanApplicationStatus status) {
        return switch (status) {
            case NONE -> Status.NONE;
            case NEW -> Status.NEW;
            case IN_PROGRESS -> Status.IN_PROGRESS;
            case APPROVED -> Status.APPROVED;
            case REJECTED -> Status.REJECTED;
            case EXPIRED -> Status.EXPIRED;
        };
    }

    public LoanApplicationStatus toDb(Status status) {
        return switch (status) {
            case NONE -> LoanApplicationStatus.NONE;
            case NEW -> LoanApplicationStatus.NEW;
            case IN_PROGRESS -> LoanApplicationStatus.IN_PROGRESS;
            case APPROVED -> LoanApplicationStatus.APPROVED;
            case REJECTED -> LoanApplicationStatus.REJECTED;
            case EXPIRED -> LoanApplicationStatus.EXPIRED;
        };
    }
}
