package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.UserAdditionalData;
import org.jooq.Record;
import org.jooq.RecordMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.financial.loan.persistence.model.Tables.USER_ADDITIONAL_DATA;

public class UserAdditionalDataMapper implements RecordMapper<Record, UserAdditionalData> {
    @Override
    public UserAdditionalData map(Record record) {
        return UserAdditionalData.create(
                record.get(USER_ADDITIONAL_DATA.USER_ID),
                LocalDateTime.of(
                        record.get(USER_ADDITIONAL_DATA.BIRTHDAY),
                        LocalTime.of(0, 0)
                ),
                null,
                BigDecimal.valueOf(
                        record.get(USER_ADDITIONAL_DATA.MONTHLY_INCOME)
                ),
                record.get(USER_ADDITIONAL_DATA.PASSPORT_SERIES).concat(
                        record.get(USER_ADDITIONAL_DATA.PASSPORT_NUMBER)
                )
        ).getValue();
    }
}
