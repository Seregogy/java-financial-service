package com.financial.loan.auto.dto.response;

/**
 * Элемент разбивки отказов по причине в выбранном периоде.
 *
 * @param reason текст причины отказа
 * @param count количество заявок с данной причиной
 */
public record RejectionReasonStats(String reason, long count) {
}
