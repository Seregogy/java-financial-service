package com.financial.loan.domain.entity.interfaces;

import com.financial.loan.domain.entity.ValueObject.Passport;
import com.financial.loan.domain.entity.entity.UserAdditionalData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserAdditionalDataRepository {


    Passport getPassportByUserId(UUID user);


    //снизу бред
    UserAdditionalDataRepository getById(UUID userAdditionalDataId);

    UUID createUserAdditionalData(UserAdditionalData entity);

    UUID updateUserAdditionalData(
            UUID userId,
            LocalDateTime birthday,
            String password,
            BigDecimal monthlyIncome
    );



    UUID deleteUserAdditionalData(UUID userAdditionalDataId);
}
