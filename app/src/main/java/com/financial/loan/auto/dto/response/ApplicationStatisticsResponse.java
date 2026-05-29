package com.financial.loan.auto.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Агрегированная статистика по кредитным заявкам за период.
 *
 * @param totalApplications общее количество заявок в периоде
 * @param approvedCount количество одобренных заявок
 * @param rejectedCount количество отказанных заявок
 * @param rejectionsByReason разбивка отказов по причинам
 * @param averageProcessingHours среднее время обработки заявки в часах
 * @param approvalConversionPercent конверсия в одобрение, в процентах (0–100)
 */
public record ApplicationStatisticsResponse(
        long totalApplications,
        long approvedCount,
        long rejectedCount,
        List<RejectionReasonStats> rejectionsByReason,
        BigDecimal averageProcessingHours,
        BigDecimal approvalConversionPercent
) {
}
