package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.enums.Role;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;

    public UUID execute(String fullName, Role role) {
        
        User user = User.create(fullName, role);

        userRepository.create(user);

        return user.getId();
    }
}