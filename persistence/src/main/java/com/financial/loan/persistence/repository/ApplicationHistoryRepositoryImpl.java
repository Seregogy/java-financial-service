package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.ApplicationHistory;
import com.financial.loan.domain.interfaces.ApplicationHistoryRepository;
import com.financial.loan.persistence.mapper.ApplicationHistoryMapper;
import com.financial.loan.persistence.mapper.StatusMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static com.financial.loan.persistence.model.Tables.APPLICATION_HISTORY;
import static com.financial.loan.persistence.model.Tables.CAR;

@RequiredArgsConstructor
public class ApplicationHistoryRepositoryImpl implements ApplicationHistoryRepository {
    private final DSLContext context;
    private final ApplicationHistoryMapper applicationHistoryMapper;
    private final StatusMapper statusMapper;

    @Override
    public List<ApplicationHistory> getAll(int page, int size) {
        return context.selectFrom(APPLICATION_HISTORY)
                .offset(page * size)
                .limit(size)
                .fetch()
                .map(applicationHistoryMapper);
    }

    @Override
    public ApplicationHistory getById(UUID applicationHistoryId) {
        return context.selectFrom(APPLICATION_HISTORY)
                .where(APPLICATION_HISTORY.APPLICATION_ID.eq(applicationHistoryId))
                .fetchOne()
                .map(applicationHistoryMapper);
    }

    @Override
    public UUID create(ApplicationHistory entity) {
        return context.insertInto(APPLICATION_HISTORY)
                .set(APPLICATION_HISTORY.ID, entity.getId())
                .set(APPLICATION_HISTORY.APPLICATION_ID, entity.getApplicationId())
                .set(APPLICATION_HISTORY.OLD_STATUS, statusMapper.toDb(entity.getOldStatus()))
                .set(APPLICATION_HISTORY.NEW_STATUS, statusMapper.toDb(entity.getNewStatus()))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID update(UUID idLoan, ApplicationHistory loan) {
        return context.update(APPLICATION_HISTORY)
                .set(APPLICATION_HISTORY.ID, loan.getId())
                .set(APPLICATION_HISTORY.APPLICATION_ID, loan.getApplicationId())
                .set(APPLICATION_HISTORY.OLD_STATUS, statusMapper.toDb(loan.getOldStatus()))
                .set(APPLICATION_HISTORY.NEW_STATUS, statusMapper.toDb(loan.getNewStatus()))
                .where(APPLICATION_HISTORY.ID.eq(idLoan))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID delete(UUID ApplicationHistoryId) {
        context.deleteFrom(APPLICATION_HISTORY)
                .where(APPLICATION_HISTORY.ID.eq(ApplicationHistoryId))
                .execute();

        return ApplicationHistoryId;
    }
}