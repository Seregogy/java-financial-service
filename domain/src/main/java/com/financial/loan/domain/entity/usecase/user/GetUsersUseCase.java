package com.financial.loan.domain.entity.usecase.user;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetUsersUseCase {
    private final UserRepository userRepository;

    public List<User> execute() {
        return userRepository.getUsers();
    }
}
