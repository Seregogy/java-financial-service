package com.financial.loan.auto.controller;

import com.financial.loan.auto.dto.request.UpdateApplicationStatusRequest;
import com.financial.loan.auto.dto.response.ApplicationStatusUpdatedResponse;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.enums.Status;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * REST-контроллер операций с кредитными заявками.
 */
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    /**
     * Сценарий смены статуса заявки: проверка роли в БД, бизнес-правила переходов,
     * запись в {@link com.financial.loan.domain.entity.ApplicationHistory}.
     */
    @FunctionalInterface
    public interface UpdateApplicationStatusUseCase {

        /**
         * Выполняет смену статуса заявки.
         *
         * @param applicationId идентификатор заявки
         * @param employeeId идентификатор сотрудника (кредитного специалиста)
         * @param newStatus целевой доменный статус
         * @param rejectionReason причина отказа (для {@link Status#REJECTED}); иначе может быть {@code null}
         * @return обновлённая заявка или сообщение об ошибке
         */
        Result<LoanApplication> execute(
                UUID applicationId,
                UUID employeeId,
                Status newStatus,
                String rejectionReason
        );
    }

    private final UpdateApplicationStatusUseCase updateApplicationStatusUseCase;

    public ApplicationController(UpdateApplicationStatusUseCase updateApplicationStatusUseCase) {
        this.updateApplicationStatusUseCase = updateApplicationStatusUseCase;
    }

    /**
     * Частичное обновление статуса заявки (PATCH).
     *
     * <p>Доступно только роли «кредитный специалист»: ожидается authority {@code ROLE_CREDIT_SPECIALIST}
     * (или эквивалент после маппинга JWT), согласованный с фактической ролью пользователя в БД
     * в слое безопасности / use case.</p>
     *
     * <p>Бизнес-правила переходов: из «Новая» только в «В работе»; из «В работе» — в «Одобрена» или «Отказана»;
     * при отказе требуется {@code rejectionReason}.</p>
     *
     * @param applicationId идентификатор заявки
     * @param request данные запроса
     * @return 200 и {@link ApplicationStatusUpdatedResponse} при успехе; 400 при ошибке домена
     */
    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasAuthority('ROLE_CREDIT_SPECIALIST')")
    public ResponseEntity<?> updateStatus(
            @PathVariable UUID applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        Status domainStatus = request.newStatus().toDomainStatus();
        Result<LoanApplication> result = updateApplicationStatusUseCase.execute(
                applicationId,
                request.employeeId(),
                domainStatus,
                request.rejectionReason()
        );

        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(Map.of("error", result.getError()));
        }

        LoanApplication application = result.getValue();
        ApplicationStatusUpdatedResponse body = new ApplicationStatusUpdatedResponse(
                application.getId(),
                request.newStatus().name(),
                application.getUpdated()
        );
        return ResponseEntity.ok(body);
    }
}
