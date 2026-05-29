package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.domain.interfaces.LoanApplicationRepository;
import com.financial.loan.persistence.mapper.LoanApplicationMapper;
import com.financial.loan.persistence.mapper.StatusMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

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
    public List<LoanApplication> getAll(int page, int size) {
        return context.selectFrom(LOAN_APPLICATION)
                .offset(page * size)
                .limit(size)
                .fetch()
                .map(loanMapper);
    }

    @Override
    public LoanApplication getById(UUID loanApplicationId) {
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
                .set(LOAN_APPLICATION.STATUS, new StatusMapper().toDb(entity.getStatus()))
                .set(LOAN_APPLICATION.CREATED_AT, LocalDateTime.now())
                .set(LOAN_APPLICATION.UPDATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne()
                .get(LOAN_APPLICATION.ID);
    }

    @Override
    public UUID update(
            UUID loanApplicationId,
            LoanApplication loanApplication
    ) {

        int termMonths = loanApplication
                .getTerm()
                .getMonthValue() - LocalDateTime.now().getMonthValue();

        if (termMonths <= 0)
            termMonths = 1;

        return context.update(LOAN_APPLICATION)
                .set(LOAN_APPLICATION.CAR_ID, loanApplication.getCarId())
                .set(LOAN_APPLICATION.USER_ID, loanApplication.getUserId())
                .set(LOAN_APPLICATION.LOAN_AMOUNT, loanApplication.getLoanAmount().doubleValue())
                .set(LOAN_APPLICATION.FIRST_PAYMENT, loanApplication.getFirstPayment().doubleValue())
                .set(LOAN_APPLICATION.TERM_MONTH, termMonths)
                .set(LOAN_APPLICATION.UPDATED_AT, LocalDateTime.now())
                .where(LOAN_APPLICATION.ID.eq(loanApplicationId))
                .returning()
                .fetchOne()
                .get(LOAN_APPLICATION.ID);
    }

    @Override
    public List<LoanApplication> getByIdUser(UUID userId) {
        return context.selectFrom(LOAN_APPLICATION)
                .where(LOAN_APPLICATION.USER_ID.eq(userId))
                .fetch()
                .map(loanMapper);
    }

    @Override
    public UUID delete(UUID loanApplicationId) {
        context.deleteFrom(LOAN_APPLICATION)
                .where(LOAN_APPLICATION.ID.eq(loanApplicationId))
                .execute();
        return loanApplicationId;
    }
}