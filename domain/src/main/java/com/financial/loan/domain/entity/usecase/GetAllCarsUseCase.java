package com.financial.loan.domain.entity.usecase;

import com.financial.loan.domain.entity.Car;
import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.interfaces.CarRepository;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class GetAllCarsUseCase {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public Result<List<Car>> execute(UUID adminId) {
        User user = userRepository.findById(adminId);
        if (!user.getRole().equals("ADMIN") && !user.getRole().equals("MANAGER")) {
            return Result.failure("Нет прав доступа к списку всех автомобилей");
        }

        return Result.success(carRepository.getAll());
    }
}