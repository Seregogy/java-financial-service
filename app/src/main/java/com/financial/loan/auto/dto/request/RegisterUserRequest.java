package com.financial.loan.auto.dto.request;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RegisterUserRequest {
    private String email;
    private String password;
    private String fullName;
    private Role role;

    public User toDomain() {
        return User.builder()
                .email(email)
                .fullName(fullName)
                .role(role)
                .build();
    }
}
