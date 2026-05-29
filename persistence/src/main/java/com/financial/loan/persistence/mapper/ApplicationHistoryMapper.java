package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.ApplicationHistory;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.util.UUID;

import static com.financial.loan.persistence.model.Tables.APPLICATION_HISTORY;

@RequiredArgsConstructor
public class ApplicationHistoryMapper implements RecordMapper<Record, ApplicationHistory> {
    private final StatusMapper statusMapper;

    @Override
    public ApplicationHistory map(Record record) {
        return ApplicationHistory.create(
                record.get(APPLICATION_HISTORY.APPLICATION_ID),
                statusMapper.toDomain(
                        record.get(APPLICATION_HISTORY.OLD_STATUS)
                ),
                statusMapper.toDomain(
                        record.get(APPLICATION_HISTORY.NEW_STATUS)
                ),
                new UUID(0L, 0L),
                record.get(APPLICATION_HISTORY.CHANGED_AT)
        );
    }
}