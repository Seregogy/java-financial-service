package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.domainexception.InvalidCredentialsException;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.interfaces.UserRepository;
import com.financial.loan.domain.service.PasswordEncodeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUserUseCase {
    private final PasswordEncodeService passwordEncodeService;
    private final UserRepository userRepository;

    public User execute(String email, String rawPassword)
            throws UserNotFoundException, InvalidCredentialsException {

        if (!passwordEncodeService.checkPassword(rawPassword, userRepository.getUserPasswordByEmail(email)))
            throw new InvalidCredentialsException("Invalid email or password");

        return userRepository.getUserByEmail(email);
    }
}
