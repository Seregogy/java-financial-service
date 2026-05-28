package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeleteUserUseCase {
    private final UserRepository userRepository;

    public Result<UUID> execute(UUID userId) {
        userRepository.delete(userId);

        return Result.success(userId);
    }
}
