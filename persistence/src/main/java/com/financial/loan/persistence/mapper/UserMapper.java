package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.User;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static com.financial.loan.persistence.model.tables.Users.USERS;


public class UserMapper implements RecordMapper<Record, User> {
    @Override
    public User map(Record record) {
        return User.builder()
                .id(record.get(USERS.ID))
                .fullName(record.get(USERS.NAME))
                .role(UserRoleMapper.toDomain(
                    record.get(USERS.ROLE)
                ))
                .createdAt(record.get(USERS.CREATED_AT))
                .updatedAt(record.get(USERS.UPDATED_AT))
                .build();
    }
}
