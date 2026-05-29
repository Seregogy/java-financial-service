package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.LoanApplication;
import com.financial.loan.persistence.model.tables.records.CarRecord;
import org.jooq.RecordMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.Record;
import static com.financial.loan.persistence.model.tables.LoanApplication.LOAN_APPLICATION;

public class LoanApplicationMapper implements RecordMapper<org.jooq.Record, LoanApplication> {

    @Override
    public LoanApplication map(Record record) {
        Integer termMonths = record.get(LOAN_APPLICATION.TERM_MONTH);
        LocalDateTime termDateTime = LocalDateTime.now().plusMonths(record.get(LOAN_APPLICATION.TERM_MONTH) != null ? termMonths : 0);

        return LoanApplication.create(
                record.get(LOAN_APPLICATION.CAR_ID),
                record.get(LOAN_APPLICATION.USER_ID),
                BigDecimal.valueOf(record.get(LOAN_APPLICATION.LOAN_AMOUNT)),
                BigDecimal.valueOf(record.get(LOAN_APPLICATION.FIRST_PAYMENT)),
                LocalDateTime.of(0, record.get(LOAN_APPLICATION.TERM_MONTH), 1,0,0)
        ).getValue();
    }
}