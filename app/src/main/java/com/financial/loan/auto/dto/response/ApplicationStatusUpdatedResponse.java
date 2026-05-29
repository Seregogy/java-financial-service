package com.financial.loan.auto.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ответ при успешной смене статуса заявки.
 *
 * @param applicationId идентификатор заявки
 * @param newStatus установленный статус (значение API: WIP, Approved, Rejected)
 * @param updatedAt время последнего обновления заявки в доменной модели
 */
public record ApplicationStatusUpdatedResponse(
        UUID applicationId,
        String newStatus,
        LocalDateTime updatedAt
) {
}
