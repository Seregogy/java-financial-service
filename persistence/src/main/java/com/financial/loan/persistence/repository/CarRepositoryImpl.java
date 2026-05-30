package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.interfaces.CarRepository;
import com.financial.loan.persistence.mapper.CarMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.financial.loan.persistence.model.Tables.LOAN_APPLICATION;
import static com.financial.loan.persistence.model.Tables.CAR;

@Builder
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {
    private final DSLContext context;
    private final CarMapper carMapper;

    @Override
    public UUID create(Car entity) {

        int yearValue = entity.getYear().getYear();
        double costValue = entity.getCost().doubleValue();

        return context.insertInto(CAR)
                .set(CAR.BRAND, entity.getBrand())
                .set(CAR.MODEL, entity.getModel())
                .set(CAR.YEAR, yearValue)
                .set(CAR.COST, costValue)
                .returning()
                .fetchOne()
                .get(CAR.ID);
    }

    /**
    * TODO: Тут тоже нужна пагинация!!!
    * */
    @Override
    public List<Car> getAll() {
        return context.selectFrom(CAR)
                .fetch()
                .map(carMapper);
    }

//    @Override
//    public Car getByLoan(UUID idLoan) {
//        return context.select(CAR.fields())
//                .from(CAR)
//                .join(com.financial.loan.persistence.model.Tables.LOAN_APPLICATION)
//                .on(CAR.ID.eq(com.financial.loan.persistence.model.Tables.LOAN_APPLICATION.CAR_ID))
//                .where(com.financial.loan.persistence.model.Tables.LOAN_APPLICATION.ID.eq(idLoan))
//                .fetchOne()
//                .map(carMapper);
//    }

    @Override
    public Car getByLoan(UUID idLoan) {
        return context.selectFrom(CAR)
                .where(CAR.ID.in(
                        context.select(LOAN_APPLICATION.CAR_ID)
                                .from(LOAN_APPLICATION)
                                .where(LOAN_APPLICATION.ID.eq(idLoan))
                ))
                .fetchOne()
                .map(carMapper);
    }

    @Override
    public UUID Delete(UUID idCar) {
        context.deleteFrom(CAR)
                .where(CAR.ID.eq(idCar))
                .execute();
        return idCar;
    }

    @Override
    public UUID Update(UUID idCar, Car car) {

        int yearValue = car.getYear().getYear();
        double costValue = car.getCost().doubleValue();

        return context.update(CAR)
                .set(CAR.BRAND, car.getBrand())
                .set(CAR.MODEL, car.getModel())
                .set(CAR.YEAR, yearValue)
                .set(CAR.COST, costValue)
                .set(CAR.UPDATED_AT, LocalDateTime.now())
                .where(CAR.ID.eq(idCar))
                .returning()
                .fetchOne()
                .get(CAR.ID);
    }
}