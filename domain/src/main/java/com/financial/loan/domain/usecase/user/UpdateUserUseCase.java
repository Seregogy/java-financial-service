package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;

    public UUID execute(UUID userId, String fullName, Role role) {
        if (userId == null) {
            throw new UserNotFoundException("null");
        }

        User user = User.create(fullName, "test@gmail.com", role);

        userRepository.update(userId, user.getFullName(), user.getRole());

        return userId;
    }
}