package com.financial.loan.domain.interfaces;

import com.financial.loan.domain.ValueObject.Passport;
import com.financial.loan.domain.entity.UserAdditionalData;
import com.financial.loan.domain.exception.UserAdditionalDataAlreadyExists;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserAdditionalDataRepository {
    UserAdditionalData getById(UUID userId);

    UUID createUserAdditionalData(
            UUID userId,
            LocalDateTime birthday,
            String passwordHash,
            BigDecimal monthlyIncome,
            String passport
    ) throws UserAdditionalDataAlreadyExists;

    UserAdditionalData updateUserAdditionalData(
        UserAdditionalData entity
    );

    Passport getPassportByUserId(UUID userId);

    UUID deleteAdditionalDataForUser(UUID userAdditionalDataId);
}
