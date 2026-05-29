package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.enums.Role;
import com.financial.loan.domain.interfaces.UserRepository;
import com.financial.loan.persistence.mapper.UserMapper;
import com.financial.loan.persistence.mapper.UserRoleMapper;
import com.financial.loan.persistence.model.tables.Users;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static com.financial.loan.persistence.model.Tables.USERS;

@Builder
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final DSLContext context;
    private final UserMapper userMapper;

    /**
     * @return TODO: Все пользователи из таблицы. Нужна пагинация!!!
     */
    @Override
    public List<User> getUsers() {
        return context.selectFrom(USERS)
                .fetch()
                .map(userMapper);
    }

    @Override
    public User getUserById(UUID userId) {
        int a = 10;
        return context.selectFrom(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOne()
                .map(userMapper);
    }

    @Override
    public String getUserPasswordByEmail(String email) {
        return context.select(USERS.PASSWORD)
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne()
                .get(Users.USERS.PASSWORD);
    }

    @Override
    public User getUserByEmail(String email) {
        return context.selectFrom(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne()
                .map(userMapper);
    }

    @Override
    public UUID create(User entity, String encodedPassword) {
        String[] fullName = entity.getFullName().split(" ");

        return context.insertInto(USERS)
                .set(USERS.SURNAME, fullName[0])
                .set(USERS.NAME, fullName[1])
                .set(USERS.PATRONYMIC, fullName[2])
                .set(USERS.EMAIL, entity.getEmail())
                .set(USERS.ROLE, UserRoleMapper.toDb(entity.getRole()))
                .set(USERS.PASSWORD, encodedPassword)
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID update(UUID userId, String fullName, Role role) {
        String[] fullNameSplitted = fullName.split(" ");
        return context.update(USERS)
                .set(USERS.SURNAME, fullNameSplitted[0])
                .set(USERS.NAME, fullNameSplitted[1])
                .set(USERS.PATRONYMIC, fullNameSplitted[2])
                .where(USERS.ID.eq(userId))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID delete(UUID userId) {
        context.delete(USERS)
                .where(USERS.ID.eq(userId));

        return userId;
    }
}
