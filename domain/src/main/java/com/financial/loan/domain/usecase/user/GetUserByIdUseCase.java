package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class GetUserByIdUseCase {
    private final UserRepository userRepository;

    public User execute(UUID userId) {
        if (userId == null) {
            throw new UserNotFoundException("null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        return user;
    }
}