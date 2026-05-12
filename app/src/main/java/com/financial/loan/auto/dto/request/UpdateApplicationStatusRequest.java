package com.financial.loan.auto.dto.request;

import com.financial.loan.domain.entity.enums.Status;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Тело запроса на смену статуса кредитной заявки.
 *
 * <p>Переходы статусов и обязательность {@code rejectionReason} при отказе
 * дополнительно проверяются в доменном сценарии {@code UpdateApplicationStatusUseCase}.</p>
 *
 * @param employeeId идентификатор сотрудника, выполняющего операцию (кредитный специалист)
 * @param newStatus целевой статус в терминах API
 * @param rejectionReason причина отказа; обязательна только при {@link ApplicationApiStatus#Rejected}
 */
public record UpdateApplicationStatusRequest(
        @NotNull(message = "employeeId обязателен")
        UUID employeeId,
        @NotNull(message = "newStatus обязателен")
        ApplicationApiStatus newStatus,
        String rejectionReason
) {

    /**
     * Значения статуса в API (ТЗ: WIP / Approved / Rejected).
     */
    public enum ApplicationApiStatus {
        /** В работе (доменный {@link Status#IN_PROGRESS}). */
        WIP,
        /** Одобрена. */
        Approved,
        /** Отказана. */
        Rejected;

        /**
         * Сопоставление значения API с доменным перечислением {@link Status}.
         *
         * @return доменный статус
         */
        public Status toDomainStatus() {
            return switch (this) {
                case WIP -> Status.IN_PROGRESS;
                case Approved -> Status.APPROVED;
                case Rejected -> Status.REJECTED;
            };
        }
    }

    @AssertTrue(message = "При статусе Rejected поле rejectionReason обязательно и не может быть пустым")
    public boolean isRejectionReasonValidForRejected() {
        if (newStatus != ApplicationApiStatus.Rejected) {
            return true;
        }
        return rejectionReason != null && !rejectionReason.isBlank();
    }
}
