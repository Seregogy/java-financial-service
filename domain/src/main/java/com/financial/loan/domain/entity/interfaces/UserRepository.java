package com.financial.loan.domain.entity.interfaces;

import com.financial.loan.domain.entity.entity.User;
import com.financial.loan.domain.entity.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<User> getUsers();

    User getUserById(UUID userId);

    UUID create(User entity);

    UUID update(
            UUID userId,
            String fullName,
            Role role
    );

    UUID delete(UUID userId);
}
