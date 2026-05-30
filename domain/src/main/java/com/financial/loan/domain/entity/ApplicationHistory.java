package com.financial.loan.domain.entity;

import com.financial.loan.domain.domainexception.validation.ApplicationHistoryValidationException;
import com.financial.loan.domain.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationHistory {
    private final UUID id;
    private final UUID applicationId;
    private final Status oldStatus;
    private final Status newStatus;
    private final UUID changedBy;
    private final LocalDateTime changedAt;

    public static ApplicationHistory create(
            UUID applicationId,
            Status oldStatus,
            Status newStatus,
            UUID changedBy,
            LocalDateTime changedAt) {

        validateApplicationId(applicationId);
        validateStatuses(oldStatus, newStatus);
        validateChangedBy(changedBy);

        LocalDateTime timestamp = changedAt != null ? changedAt : LocalDateTime.now();

        return new ApplicationHistory(
                UUID.randomUUID(),
                applicationId,
                oldStatus,
                newStatus,
                changedBy,
                timestamp
        );
    }

    private static void validateApplicationId(UUID applicationId) {
        if (applicationId == null) {
            throw new ApplicationHistoryValidationException("applicationId", "applicationId cannot be null");
        }
    }

    private static void validateStatuses(Status oldStatus, Status newStatus) {
        if (oldStatus == null) {
            throw new ApplicationHistoryValidationException("oldStatus", "oldStatus cannot be null");
        }
        if (newStatus == null) {
            throw new ApplicationHistoryValidationException("newStatus", "newStatus cannot be null");
        }
        if (oldStatus == newStatus) {
            throw new ApplicationHistoryValidationException("statuses", "oldStatus and newStatus cannot be the same");
        }
    }

    private static void validateChangedBy(UUID changedBy) {
        if (changedBy == null) {
            throw new ApplicationHistoryValidationException("changedBy", "changedBy cannot be null");
        }
    }
}