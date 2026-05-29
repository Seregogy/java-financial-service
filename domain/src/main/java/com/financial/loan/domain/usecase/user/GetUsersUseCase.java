package com.financial.loan.domain.usecase.user;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetUsersUseCase {
    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.getUsers();
    }
}
