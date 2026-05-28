package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GetUserByIdUseCase {
    private final UserRepository userRepository;

    public User execute(UUID userId) {
        return userRepository.getUserById(userId);
    }
}
