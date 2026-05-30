package com.financial.loan.auto.controller;

import com.financial.loan.auto.jwt.AuthUser;
import com.financial.loan.domain.domainexception.ContextualUnsupportedRoleException;
import com.financial.loan.domain.entity.*;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.enums.Status;
import com.financial.loan.domain.interfaces.CarRepository;
import com.financial.loan.domain.interfaces.UserAdditionalDataRepository;
import com.financial.loan.domain.usecase.loan.*;
import com.financial.loan.domain.usecase.loanhistory.*;
import com.financial.loan.domain.usecase.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GodController {
    private final UserAdditionalDataRepository userAdditionalDataRepository;

    private final RequireRoleUseCase requireRoleUseCase;

    private final CreateLoanUseCase createLoanUseCase;
    private final GetLoansUseCase getLoansUseCase;
    private final GetLoanByIdUseCase getLoanByIdUseCase;
    private final GetLoansByUserIdUseCase getLoansByUserIdUseCase;
    private final ChangeStatusUseCase changeStatusUseCase;

    private final CarRepository carRepository;

    private final GetLoansHistoryUseCase getLoansHistoryUseCase;


    public record UserAdditionalDataRequest(
            LocalDateTime birthday,
            BigDecimal monthlyIncome,
            String passport
    ) { }

    @PostMapping("/users/additional-data")
    public ResponseEntity<?> createUserAdditionalData(
            @AuthUser UUID userId,
            @RequestBody UserAdditionalDataRequest request
    ) {

        return ResponseEntity.ok(Map.of(
                "id", userAdditionalDataRepository.createUserAdditionalData(
                                userId,
                                request.birthday,
                                "",
                                request.monthlyIncome,
                                request.passport
                        )
                )
        );
    }

    @PostMapping("/applications")
    public ResponseEntity<?> createApplication(
            @AuthUser UUID userId,
            @RequestBody CreateApplicationRequest request
    ) throws ContextualUnsupportedRoleException {
        LoanApplication loan = createLoanUseCase.execute(
                request.carData().carId(),
                userId,
                request.loanParameters().amount(),
                request.loanParameters().downPayment(),
                LocalDateTime.now().plusMonths(request.loanParameters().termMonths())
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "applicationId", loan.getId().toString(),
                        "status", loan.getStatus(),
                        "createdAt", LocalDateTime.now()
                ));
    }

    @PostMapping("/cars")
    public ResponseEntity<?> createCar(
            @RequestBody CreateCarData carData
    ) {
        UUID id = carRepository.create(
                Car.create(
                        carData.brand,
                        carData.model,
                        carData.year,
                        carData.cost
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("carId", id.toString()));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<?> getMyApplications(
            @AuthUser UUID userId,
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<LoanApplication> loans = getLoansByUserIdUseCase.execute(userId);

        if (status != null) {
            loans = loans.stream()
                    .filter(loan -> loan.getStatus() == status)
                    .toList();
        }

        int start = page * size;
        int end = Math.min(start + size, loans.size());
        List<LoanApplication> paged = loans.subList(start, end);

        List<Map<String, String>> content = paged.stream()
                .map(loan -> Map.of(
                        "applicationId", loan.getId().toString(),
                        "status", loan.getStatus().name(),
                        "createdAt", loan.getCreated().toString(),
                        "updatedAt", loan.getUpdated().toString(),
                        "carInfo",  loan.getCarId() + " " + "Car" + " " + "price",
                        "loanAmount", loan.getLoanAmount().toString()
                ))
                .toList();

        return ResponseEntity.ok(Map.of(
                "content", content,
                "totalElements", loans.size(),
                "totalPages", (int) Math.ceil((double) loans.size() / size)
        ));
    }

    @GetMapping("/my-applications/{applicationId}")
    public ResponseEntity<?> getMyApplicationById(
            @AuthUser UUID userId,
            @PathVariable UUID applicationId
    ) {
        LoanApplication loan = getLoanByIdUseCase.execute(applicationId);

        if (!loan.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Доступ запрещен"));
        }

        return ResponseEntity.ok(mapToApplicationDetails(loan));
    }


    @GetMapping("/applications/search")
    public ResponseEntity<?> searchApplications(
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) UUID moderatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<LoanApplication> loans = getLoansUseCase.execute(page, size);

        List<Map<String, Object>> content = loans.stream()
                .map(this::mapToApplicationListItem)
                .toList();

        return ResponseEntity.ok(Map.of(
                "content", content,
                "totalElements", loans.size()
        ));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<?> getApplicationDetailsModerator(@PathVariable UUID applicationId) {
        LoanApplication loan = getLoanByIdUseCase.execute(applicationId);
        return ResponseEntity.ok(mapToApplicationDetails(loan));
    }

    @PostMapping("/applications/{applicationId}/take")
    public ResponseEntity<?> takeApplication(
            @AuthUser UUID userId,
            @PathVariable UUID applicationId
    ) {
       // requireRoleUseCase.execute(userId, Role.MODERATOR);

        UUID loanId = changeStatusUseCase
                .execute(applicationId, Status.IN_PROGRESS, userId, null);

        LoanApplication loanApplication = getLoanByIdUseCase.execute(loanId);

        return ResponseEntity.ok(Map.of("status", loanApplication.getStatus().name()));
    }

    @PostMapping("/applications/{applicationId}/approve")
    public ResponseEntity<?> approveApplication(
            @AuthUser UUID userId,
            @PathVariable UUID applicationId
    ) {
        //requireRoleUseCase.execute(userId, Role.MODERATOR);

        UUID loanId = changeStatusUseCase
                .execute(applicationId, Status.APPROVED, userId, null);

        LoanApplication loanApplication = getLoanByIdUseCase.execute(loanId);

        return ResponseEntity.ok(Map.of("status", loanApplication.getStatus().name()));
    }

    @PostMapping("/applications/{applicationId}/reject")
    public ResponseEntity<?> rejectApplication(
            @AuthUser UUID userId,
            @PathVariable UUID applicationId
    ) {
        //requireRoleUseCase.execute(userId, Role.MODERATOR);

        UUID loanId = changeStatusUseCase
                .execute(applicationId, Status.REJECTED, userId, null);

        LoanApplication loanApplication = getLoanByIdUseCase.execute(loanId);

        return ResponseEntity.ok(Map.of("status", loanApplication.getStatus().name()));
    }


    @GetMapping("/admin/applications/{applicationId}/history")
    public ResponseEntity<List<HistoryEntryResponse>> getApplicationHistory(@PathVariable UUID applicationId) {
        List<ApplicationHistory> histories = getLoansHistoryUseCase.execute(0, 100);
        // Фильтрация по applicationId

        List<HistoryEntryResponse> response = histories.stream()
                .filter(h -> h.getApplicationId().equals(applicationId))
                .map(this::mapToHistoryEntry)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<?> getStatistics(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) UUID moderatorId
    ) {
        List<LoanApplication> loans = getLoansUseCase.execute(0, Integer.MAX_VALUE);

        long total = loans.size();
        long approved = loans.stream().filter(l -> l.getStatus() == Status.APPROVED).count();
        long rejected = loans.stream().filter(l -> l.getStatus() == Status.REJECTED).count();

        Map<String, Long> rejectionReasons = new HashMap<>();
        rejectionReasons.put("Недостаточный доход", 200L);
        rejectionReasons.put("Низкий скоринговый балл", 150L);

        double conversionRate = total > 0 ? (approved * 100.0 / total) : 0;

        return ResponseEntity.ok(Map.of(
                "totalApplications", total,
                "approvedCount", approved,
                "rejectedCount", rejected,
                "rejectionReasons", rejectionReasons,
                "averageProcessingTimeHours", 48.5,
                "conversionRate", conversionRate
        ));
    }

    @PutMapping("/admin/applications/{applicationId}/status")
    public ResponseEntity<?> adminChangeStatus(
            @PathVariable UUID applicationId,
            @RequestBody AdminStatusChangeRequest request
    ) {
        LoanApplication loan = getLoanByIdUseCase.execute(applicationId);
        Status oldStatus = loan.getStatus();
        Status newStatus = request.newStatus();

        changeStatusUseCase.execute(applicationId, newStatus, request.adminId(), request.comment());

        return ResponseEntity.ok(Map.of(
                "applicationId", applicationId.toString(),
                "oldStatus", oldStatus.name(),
                "newStatus", newStatus.name()
        ));
    }


    @PostMapping("/system/check-expired")
    public ResponseEntity<?> checkExpiredApplications() {
        // Логика проверки просроченных заявок
        return ResponseEntity.ok(Map.of(
                "expiredCount", 0,
                "notified", true
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exc) {
        return ResponseEntity.internalServerError().body(Map.of("error-message", exc.getMessage()));
    }

    // ==================== HELPER METHODS ====================

    private Map<String, Object> mapToApplicationListItem(LoanApplication loan) {
        return Map.of(
                "id", loan.getId().toString(),
                "maskedPassport", "**********",
                "status", loan.getStatus().name(),
                "createdAt", loan.getCreated()
        );
    }

    private Map<String, Object> mapToApplicationDetails(LoanApplication loan) {
        return Map.of(
                "id", loan.getId().toString(),
                "maskedPassport", "**********",
                "status", loan.getStatus().name()
        );
    }

    private HistoryEntryResponse mapToHistoryEntry(ApplicationHistory history) {
        return new HistoryEntryResponse(
                history.getId().toString(),
                history.getApplicationId().toString(),
                history.getChangedBy().toString(),
                history.getOldStatus() != null ? history.getOldStatus().name() : null,
                history.getNewStatus().name(),
                "empty",
                history.getChangedAt()
        );
    }

    private String generateJwtToken(User user) {
        return "dummy-token-for-development";
    }


    public record RegisterRequest(String email, String password, String fullName, String role) {}
    public record LoginRequest(String email, String password) {}

    public record CreateApplicationRequest(
            PersonalData personalData,
            FinancialData financialData,
            CarData carData,
            LoanParameters loanParameters
    ) {}

    public record PersonalData(String fullName, LocalDate birthDate, String passport) {}
    public record FinancialData(BigDecimal monthlyIncome) {}
    public record CarData(String brand, String model, Integer year, BigDecimal price, UUID carId) {}
    public record LoanParameters(BigDecimal amount, BigDecimal downPayment, Integer termMonths) {}

    public record TakeApplicationRequest(UUID employeeId) {}
    public record ApproveApplicationRequest(UUID moderatorId) {}
    public record RejectApplicationRequest(UUID moderatorId, String reason) {}
    public record AdminStatusChangeRequest(UUID adminId, Status newStatus, String comment) {}

    public record CreateCarData(
            UUID id,
            String brand,
            String model,
            LocalDateTime year,
            BigDecimal cost
    ) {}

    public record HistoryEntryResponse(
            String id,
            String applicationId,
            String userId,
            String oldStatus,
            String newStatus,
            String comment,
            LocalDateTime timestamp
    ) {}
}
