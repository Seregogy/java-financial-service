package com.financial.loan.domain.entity.usecase;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.interfaces.CarRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class CreateCarUseCase {
    private final CarRepository carRepository;

    public Result<UUID> execute(Car car) {
        if (car.getBrand() == null || car.getModel() == null || car.getYear() == null) {
            return Result.failure("Все поля обязательны для заполнения");
        }
        
        if (car.getCost() == null || car.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Стоимость автомобиля должна быть положительным числом");
        }

        try {
            UUID id = carRepository.create(car);
            return Result.success(id);
        } catch (Exception e) {
            return Result.failure("Ошибка при сохранении данных авто в БД");
        }
    }
}