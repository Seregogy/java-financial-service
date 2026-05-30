package com.financial.loan.auto.controller;

import com.financial.loan.auto.dto.request.RegisterUserRequest;
import com.financial.loan.auto.dto.response.LoginResponse;
import com.financial.loan.auto.dto.response.RegisterUserResponse;
import com.financial.loan.domain.domainexception.InvalidCredentialsException;
import com.financial.loan.domain.domainexception.UserNotFoundException;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.service.JwtService;
import com.financial.loan.domain.usecase.user.CreateUserUseCase;
import com.financial.loan.domain.usecase.user.LoginUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CreateUserUseCase createUser;
    private final LoginUserUseCase loginUser;
    private final JwtService jwtService;

    private static final Map<Role, UUID> dummyIds = Map.ofEntries(
        Map.entry(Role.ADMIN, UUID.fromString("00000000-0000-0000-0000-000000000002")),
        Map.entry(Role.MODERATOR, UUID.fromString("00000000-0000-0000-0000-000000000001")),
        Map.entry(Role.USER, UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest request) {
        return new RegisterUserResponse(
                createUser.execute(request.toDomain(), request.getPassword())
        );
    }


    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody RegisterUserRequest request)
            throws UserNotFoundException, InvalidCredentialsException {

        return new LoginResponse(
                jwtService.generateToken(
                        loginUser.execute(request.getEmail(), request.getPassword()).getId()
                )
        );
    }
}
