package com.financial.loan.auto.dto.response;

import com.financial.loan.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class RegisterUserResponse {
    private User user;
}
