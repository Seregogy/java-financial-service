package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.entity.enums.Status;
import com.financial.loan.domain.entity.interfaces.LoanApplicationRepository;
import com.financial.loan.persistence.mapper.LoanApplicationMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.financial.loan.persistence.model.tables.LoanApplication.LOAN_APPLICATION;

@Builder
@RequiredArgsConstructor
public class LoanApplicationRepositoryImpl implements LoanApplicationRepository {
    private final DSLContext context;
    private final LoanApplicationMapper loanMapper;

    @Override
    public List<LoanApplication> getAll() {
        return context.selectFrom(LOAN_APPLICATION)
                .fetch()
                .map(loanMapper);
    }

    @Override
    public LoanApplication get(UUID loanApplicationId) {
        return context.selectFrom(LOAN_APPLICATION)
                .where(LOAN_APPLICATION.ID.eq(loanApplicationId))
                .fetchOne()
                .map(loanMapper);
    }

    @Override
    public UUID create(LoanApplication entity) {
        int termMonths = entity.getTerm().getMonthValue() - LocalDateTime.now().getMonthValue();
        if (termMonths <= 0) termMonths = 1;

        return context.insertInto(LOAN_APPLICATION)
                .set(LOAN_APPLICATION.ID, UUID.randomUUID())
                .set(LOAN_APPLICATION.CAR_ID, entity.getCarId())
                .set(LOAN_APPLICATION.USER_ID, entity.getUserId())
                .set(LOAN_APPLICATION.LOAN_AMOUNT, entity.getLoanAmount().doubleValue())
                .set(LOAN_APPLICATION.FIRST_PAYMENT, entity.getFirstPayment().doubleValue())
                .set(LOAN_APPLICATION.TERM_MONTH, termMonths)
                .set(LOAN_APPLICATION.STATUS, entity.getStatus().toString())
                .set(LOAN_APPLICATION.CREATED_AT, LocalDateTime.now())
                .set(LOAN_APPLICATION.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne()
                .get(LOAN_APPLICATION.ID);
    }

    @Override
    public UUID update(
            UUID loanApplicationId,
            UUID carId,
            UUID userId,
            BigDecimal loanAmount,
            BigDecimal firstPayment,
            LocalDateTime term,
            Status status) {

        int termMonths = term.getMonthValue() - LocalDateTime.now().getMonthValue();
        if (termMonths <= 0) termMonths = 1;

        return context.update(LOAN_APPLICATION)
                .set(LOAN_APPLICATION.CAR_ID, carId)
                .set(LOAN_APPLICATION.USER_ID, userId)
                .set(LOAN_APPLICATION.LOAN_AMOUNT, loanAmount.doubleValue())
                .set(LOAN_APPLICATION.FIRST_PAYMENT, firstPayment.doubleValue())
                .set(LOAN_APPLICATION.TERM_MONTH, termMonths)
                .set(LOAN_APPLICATION.STATUS, status.toString())
                .set(LOAN_APPLICATION.UPDATED_AT, LocalDateTime.now())
                .where(LOAN_APPLICATION.ID.eq(loanApplicationId))
                .returning()
                .fetchOne()
                .get(LOAN_APPLICATION.ID);
    }

    @Override
    public UUID delete(UUID loanApplicationId) {
        context.deleteFrom(LOAN_APPLICATION)
                .where(LOAN_APPLICATION.ID.eq(loanApplicationId))
                .execute();
        return loanApplicationId;
    }
}