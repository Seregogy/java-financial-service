package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class DeleteUserUseCase {
    private final UserRepository userRepository;

    public UUID execute(UUID userId) {
        if (userId == null) {
            throw new UserNotFoundException("null");
        }

        userRepository.delete(userId);

        return userId;
    }
}