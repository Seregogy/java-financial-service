package com.financial.loan.persistence.repository;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.entity.enums.Role;
import com.financial.loan.domain.entity.interfaces.UserRepository;
import com.financial.loan.persistence.mapper.UserMapper;
import com.financial.loan.persistence.mapper.UserRoleMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static com.financial.loan.persistence.model.Tables.USER;

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
        return context.select(USER)
                .fetch()
                .map(userMapper);
    }

    @Override
    public User getUserById(UUID userId) {
        return context.select(USER)
                .where(USER.ID.eq(userId))
                .fetchOne()
                .map(userMapper);
    }

    @Override
    public UUID create(User entity) {
        String[] fullName = entity.getFullName().split(" ");

        return context.insertInto(USER)
                .set(USER.SURNAME, fullName[0])
                .set(USER.NAME, fullName[1])
                .set(USER.PATRONYMIC, fullName[2])
                .set(USER.ROLE, UserRoleMapper.toDb(entity.getRole()))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID update(UUID userId, String fullName, Role role) {
        String[] fullNameSplitted = fullName.split(" ");
        return context.update(USER)
                .set(USER.SURNAME, fullNameSplitted[0])
                .set(USER.NAME, fullNameSplitted[1])
                .set(USER.PATRONYMIC, fullNameSplitted[2])
                .where(USER.ID.eq(userId))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public UUID delete(UUID userId) {
        context.delete(USER)
                .where(USER.ID.eq(userId));

        return userId;
    }
}
