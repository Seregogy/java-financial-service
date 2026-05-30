package com.financial.loan.auto.di;

import com.financial.loan.domain.interfaces.*;
import com.financial.loan.domain.service.PasswordEncodeService;
import com.financial.loan.domain.usecase.loan.*;
import com.financial.loan.domain.usecase.loanhistory.GetLoansHistoryUseCase;
import com.financial.loan.domain.usecase.user.CreateUserUseCase;
import com.financial.loan.domain.usecase.user.LoginUserUseCase;
import com.financial.loan.domain.usecase.user.RequireRoleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UseCaseModule {
    @Bean
    CreateUserUseCase provideCreateUserUseCase(
            PasswordEncodeService passwordEncodeService,
            UserRepository userRepository
    ) {
        return new CreateUserUseCase(passwordEncodeService, userRepository);
    }

    @Bean
    RequireRoleUseCase provideRequireRoleUseCase(
            UserRepository userRepository
    ) {
        return new RequireRoleUseCase(userRepository);
    }

    @Bean
    LoginUserUseCase provideLoginUserUseCase(
            PasswordEncodeService passwordEncodeService,
            UserRepository userRepository
    ) {
        return new LoginUserUseCase(passwordEncodeService, userRepository);
    }


    @Bean
    CreateLoanUseCase provideCreateLoanUseCase(
            LoanApplicationRepository loanApplicationRepository,
            UserRepository userRepository,
            UserAdditionalDataRepository userAdditionalDataRepository,
            CarRepository carRepository
    ) {
        return new CreateLoanUseCase(
                loanApplicationRepository,
                userRepository,
                userAdditionalDataRepository,
                carRepository
        );
    }

    @Bean
    GetLoansUseCase provideGetLoansUseCase(
            LoanApplicationRepository loanApplicationRepository
    ) {
        return new GetLoansUseCase(loanApplicationRepository);
    }

    @Bean
    GetLoanByIdUseCase provideGetLoanByIdUseCase(
            LoanApplicationRepository loanApplicationRepository
    ) {
        return new GetLoanByIdUseCase(loanApplicationRepository);
    }

    @Bean
    GetLoansByUserIdUseCase provideGetLoansByUserIdUseCase(
            LoanApplicationRepository loanApplicationRepository
    ) {
        return new GetLoansByUserIdUseCase(loanApplicationRepository);
    }

    @Bean
    ChangeStatusUseCase provideChangeStatusUseCase(
            LoanApplicationRepository loanApplicationRepository,
            ApplicationHistoryRepository applicationHistoryRepository
    ) {
        return new ChangeStatusUseCase(loanApplicationRepository, applicationHistoryRepository);
    }

    @Bean
    GetLoansHistoryUseCase provideGetLoansHistoryUseCase(
            ApplicationHistoryRepository applicationHistoryRepository
    ) {
        return new GetLoansHistoryUseCase(applicationHistoryRepository);
    }
}
