package com.financial.loan.auto.di;

import com.financial.loan.auto.jwt.JwtServiceImpl;
import com.financial.loan.auto.jwt.PasswordEncodeServiceImpl;
import com.financial.loan.domain.service.JwtService;
import com.financial.loan.domain.service.PasswordEncodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ServiceModule {

    @Bean
    JwtService provideJwtService() {
        return new JwtServiceImpl();
    }

    @Bean
    PasswordEncodeService provideJwtService(
        PasswordEncoder encoder
    ) {
        return new PasswordEncodeServiceImpl(encoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
