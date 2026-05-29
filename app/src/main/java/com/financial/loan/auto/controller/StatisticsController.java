package com.financial.loan.auto.controller;

import com.financial.loan.auto.dto.response.ApplicationStatisticsResponse;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * REST-контроллер отчётности и статистики.
 */
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    /**
     * Сценарий выборки статистики по заявкам: фильтрация по периоду, учёт статусов
     * {@link Status}, расчёт среднего времени обработки и конверсии на основе {@link LoanApplication}.
     */
    @FunctionalInterface
    public interface GetApplicationStatisticsUseCase {

        /**
         * Возвращает агрегированную статистику по заявкам за закрытый календарный период
         * [{@code startDate}; {@code endDate}] (границы — по правилам реализации сценария).
         *
         * @param startDate начало периода
         * @param endDate конец периода
         * @return готовый ответ для API или сообщение об ошибке
         */
        Result<ApplicationStatisticsResponse> execute(LocalDate startDate, LocalDate endDate);
    }

    private final GetApplicationStatisticsUseCase getApplicationStatisticsUseCase;

    public StatisticsController(GetApplicationStatisticsUseCase getApplicationStatisticsUseCase) {
        this.getApplicationStatisticsUseCase = getApplicationStatisticsUseCase;
    }

    /**
     * Возвращает сводную статистику по заявкам за период.
     *
     * <p>Доступно только роли «админ»: ожидается authority {@code ROLE_ADMIN}
     * (или эквивалент после маппинга JWT), согласованный с ролью пользователя в БД.</p>
     *
     * <p>В ответе: общее число заявок, одобренные и отказанные, разбивка отказов по причинам,
     * среднее время обработки в часах, конверсия в одобрение (%).</p>
     *
     * @param startDate начало периода (формат {@code YYYY-MM-DD})
     * @param endDate конец периода (формат {@code YYYY-MM-DD})
     * @return {@code 200 OK} с телом {@link ApplicationStatisticsResponse}; {@code 400} при ошибке валидации или сценария
     */
    @GetMapping("/applications")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getApplicationStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "startDate не может быть позже endDate"));
        }

        Result<ApplicationStatisticsResponse> result = getApplicationStatisticsUseCase.execute(startDate, endDate);
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(Map.of("error", result.getError()));
        }
        return ResponseEntity.ok(result.getValue());
    }
}
