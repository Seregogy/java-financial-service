package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.enums.Role;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;

    public Result<UUID> execute(UUID userId, String fullName, Role role) {
        Result<User> userResult = User.create(fullName, role);

        if (userResult.isFailure()) {
            return Result.failure(userResult.getError());
        }

        User user = userResult.getValue();

        userRepository.update(userId, fullName, role);

        return Result.success(user.getId());
    }
}
