package com.financial.loan.domain.entity.usecase;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.interfaces.CarRepository;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public class GetCarByLoanUseCase {
    private final CarRepository carRepository;

    public Result<Car> execute(UUID loanId) {
        if (loanId == null) {
            return Result.failure("ID заявки не может быть пустым");
        }

        Car car = carRepository.getByLoan(loanId);
        if (car == null) {
            return Result.failure("Автомобиль для данной заявки не найден");
        }
        return Result.success(car);
    }
}