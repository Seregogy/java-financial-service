package com.financial.loan.persistence.repository;

import com.financial.loan.domain.ValueObject.Passport;
import com.financial.loan.domain.entity.UserAdditionalData;
import com.financial.loan.domain.exception.UserAdditionalDataAlreadyExists;
import com.financial.loan.domain.interfaces.UserAdditionalDataRepository;
import com.financial.loan.persistence.mapper.UserAdditionalDataMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record2;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.financial.loan.persistence.model.tables.UserAdditionalData.USER_ADDITIONAL_DATA;

@Builder
@RequiredArgsConstructor
public class UserAdditionalDataRepositoryImpl
        implements UserAdditionalDataRepository {

    private final DSLContext context;
    private final UserAdditionalDataMapper userAdditionalDataMapper;

    @Override
    public UserAdditionalData getById(UUID userId) {
        return context.select(USER_ADDITIONAL_DATA)
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(userId))
                .fetchOne()
                .map(userAdditionalDataMapper);
    }

    @Override
    public UUID createUserAdditionalData(
            UUID userId,
            LocalDateTime birthday,
            String password,
            BigDecimal monthlyIncome,
            String passport
    ) throws UserAdditionalDataAlreadyExists {
        String passportEscaped = passport.replaceAll("\\s", "");

        if (context.selectFrom(USER_ADDITIONAL_DATA)
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(userId))
                .fetchOne() != null
        ) {
            throw new UserAdditionalDataAlreadyExists("");
        }

        return context.insertInto(USER_ADDITIONAL_DATA)
                .set(USER_ADDITIONAL_DATA.USER_ID, userId)
                .set(USER_ADDITIONAL_DATA.BIRTHDAY, birthday.toLocalDate())
                .set(USER_ADDITIONAL_DATA.MONTHLY_INCOME, monthlyIncome.doubleValue())
                .set(USER_ADDITIONAL_DATA.PASSPORT_SERIES, passportEscaped.substring(0, 4))
                .set(USER_ADDITIONAL_DATA.PASSPORT_NUMBER, passportEscaped.substring(4))
                .returning()
                .fetchOne()
                .get(USER_ADDITIONAL_DATA.USER_ID);
    }

    @Override
    public UserAdditionalData updateUserAdditionalData(UserAdditionalData entity) {
        return context.update(USER_ADDITIONAL_DATA)
                .set(USER_ADDITIONAL_DATA.BIRTHDAY, entity.getBirthday()
                        .toLocalDate())
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(
                        entity.getUserId()
                ))
                .returning()
                .fetchOne()
                .map(userAdditionalDataMapper);
    }

    @Override
    public Passport getPassportByUserId(UUID userId) {
        Record2<String, String> data = context.select(
                        USER_ADDITIONAL_DATA.PASSPORT_SERIES,
                        USER_ADDITIONAL_DATA.PASSPORT_NUMBER
                )
                .from(USER_ADDITIONAL_DATA)
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(userId))
                .fetchOne();

        return new Passport(
                data.get(USER_ADDITIONAL_DATA.PASSPORT_SERIES),
                data.get(USER_ADDITIONAL_DATA.PASSPORT_NUMBER)
        );
    }

    @Override
    public UUID deleteAdditionalDataForUser(UUID userId) {
        UUID dataId = context.select(USER_ADDITIONAL_DATA.ID)
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(userId))
                .fetchOne()
                .get(USER_ADDITIONAL_DATA.ID);

        context.delete(USER_ADDITIONAL_DATA)
                .where(USER_ADDITIONAL_DATA.USER_ID.eq(userId))
                .execute();

        return dataId;
    }
}
