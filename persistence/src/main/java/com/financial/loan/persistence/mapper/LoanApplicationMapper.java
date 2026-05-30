package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.LoanApplication;
import org.jooq.RecordMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Record;
import static com.financial.loan.persistence.model.tables.LoanApplication.LOAN_APPLICATION;

public class LoanApplicationMapper implements RecordMapper<org.jooq.Record, LoanApplication> {

    @Override
    public LoanApplication map(Record record) {
        return LoanApplication.builder()
                .id(record.get(LOAN_APPLICATION.ID))
                .carId(record.get(LOAN_APPLICATION.CAR_ID))
                .userId(record.get(LOAN_APPLICATION.USER_ID))
                .loanAmount(
                        BigDecimal.valueOf(record.get(LOAN_APPLICATION.LOAN_AMOUNT))
                )
                .firstPayment(
                        BigDecimal.valueOf(record.get(LOAN_APPLICATION.FIRST_PAYMENT))
                )
                .term(
                        LocalDateTime.of(
                                0, record.get(LOAN_APPLICATION.TERM_MONTH), 1,0,0
                        )
                )
                .status(
                        new StatusMapper().toDomain(
                                record.get(LOAN_APPLICATION.STATUS)
                        )
                )
                .created(record.get(LOAN_APPLICATION.CREATED_AT))
                .updated(record.get(LOAN_APPLICATION.UPDATED_AT))
                .build();
    }
}