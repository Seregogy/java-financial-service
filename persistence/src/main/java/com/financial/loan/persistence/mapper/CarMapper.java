package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.persistence.model.tables.records.CarRecord;
import org.jooq.RecordMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Record;

import static com.financial.loan.persistence.model.tables.Car.CAR;

public class CarMapper implements RecordMapper<Record, Car> {

    @Override
    public Car map(Record record) {
        return Car.create(
                record.get(CAR.BRAND),
                record.get(CAR.MODEL),
                LocalDateTime.of(record.get(CAR.YEAR), 1, 1, 0, 0),
                BigDecimal.valueOf(record.get(CAR.COST))
        ).getValue();
    }
}