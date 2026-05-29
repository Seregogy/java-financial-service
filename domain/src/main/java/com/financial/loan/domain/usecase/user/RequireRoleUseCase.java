package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.domainexception.ContextualUnsupportedRoleException;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class RequireRoleUseCase {
    private final UserRepository userRepository;

    public void execute(UUID userId, Role requiredRole)
            throws ContextualUnsupportedRoleException, UserNotFoundException {

        User user = userRepository.getUserById(userId);

        if (!user.getRole().require(requiredRole))
            throw ContextualUnsupportedRoleException.builder()
                    .expectedRole(requiredRole)
                    .actualRole(user.getRole())
                    .build();
    }
}
