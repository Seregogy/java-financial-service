package com.financial.loan.domain.interfaces;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    List<User> getUsers();

    User getUserById(UUID userId);

    String getUserPasswordByEmail(String userId);
    User getUserByEmail(String email);

    UUID create(User entity, String encodedPassword);

    UUID update(
            UUID userId,
            String fullName,
            Role role
    );

    UUID delete(UUID userId);
}
