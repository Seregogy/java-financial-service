package com.financial.loan.domain.entity.usecase;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.interfaces.CarRepository;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public class UpdateCarUseCase {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public Result<UUID> execute(UUID carId, Car updatedCar, UUID userId) {
        User user = userRepository.findById(userId);    
        if (!user.getRole().equals("ADMIN")) {
            return Result.failure("Доступ запрещен: требуется роль ADMIN");
        }

        if (updatedCar.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Стоимость должна быть положительной");
        }

        UUID id = carRepository.Update(carId, updatedCar);
        return Result.success(id);
    }
}
