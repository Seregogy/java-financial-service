package com.financial.loan.domain.entity.interfaces;

import com.financial.loan.domain.entity.UserAdditionalData;
import com.financial.loan.domain.entity.exception.UserAdditionalDataAlreadyExists;

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

    UUID deleteAdditionalDataForUser(UUID userAdditionalDataId);
}
