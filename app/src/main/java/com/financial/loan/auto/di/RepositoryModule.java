package com.financial.loan.auto.di;

import com.financial.loan.domain.interfaces.*;
import com.financial.loan.persistence.mapper.*;
import com.financial.loan.persistence.repository.*;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RepositoryModule {
    @Bean
    UserRepository provideUserRepository(
            DSLContext dslContext
    ) {
        return new UserRepositoryImpl(dslContext, new UserMapper());
    }

    @Bean
    UserAdditionalDataRepository provideUserAdditionalDataRepository(
            DSLContext dslContext
    ) {
        return new UserAdditionalDataRepositoryImpl(
                dslContext, new UserAdditionalDataMapper()
        );
    }

    @Bean
    LoanApplicationRepository provideLoanApplicationRepository(
            DSLContext context
    ) {
        return new LoanApplicationRepositoryImpl(context, new LoanApplicationMapper());
    }

    @Bean
    CarRepository provideCarRepository(DSLContext context) {
        return new CarRepositoryImpl(context, new CarMapper());
    }

    @Bean
    ApplicationHistoryRepository provideApplicationHistoryRepository(
            DSLContext context
    ) {
        StatusMapper statusMapper = new StatusMapper();
        return new ApplicationHistoryRepositoryImpl(
                context, new ApplicationHistoryMapper(statusMapper), statusMapper
        );
    }
}
