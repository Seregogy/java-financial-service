package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.domainexception.UserNotFoundException;
import com.financial.loan.domain.entity.interfaces.UserRepository;
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