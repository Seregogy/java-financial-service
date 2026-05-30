package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.interfaces.UserRepository;
import com.financial.loan.domain.service.PasswordEncodeService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CreateUserUseCase {
    private final PasswordEncodeService passwordEncodeService;
    private final UserRepository userRepository;

    public User execute(User user, String rawPassword) {
        UUID userId = userRepository.create(
                user,
                passwordEncodeService.encode(rawPassword)
        );

        return userRepository.getUserById(userId);
    }
}