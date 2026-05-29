package com.financial.loan.domain.entity.usecase;

import com.financial.loan.domain.entity.Result;
import com.financial.loan.domain.entity.interfaces.CarRepository;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
public class DeleteCarUseCase {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public Result<UUID> execute(UUID carId, UUID userId) {
        User user = userRepository.findById(userId);
        if (!user.getRole().equals("ADMIN")) {
            return Result.failure("Только администратор может удалять данные");
        }

        carRepository.Delete(carId);
        return Result.success(carId);
    }
}