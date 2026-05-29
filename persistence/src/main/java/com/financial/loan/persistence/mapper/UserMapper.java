package com.financial.loan.persistence.mapper;

import com.financial.loan.domain.entity.User;
import com.financial.loan.domain.entity.enums.Role;
import org.jooq.Record;
import org.jooq.RecordMapper;

import static com.financial.loan.persistence.model.Tables.USER;


public class UserMapper implements RecordMapper<Record, User> {
    @Override
    public User map(Record record) {
        return User.builder()
                .id(record.get(USER.ID))
                .fullName(record.get(USER.NAME))
                .role(UserRoleMapper.toDomain(
                    record.get(USER.ROLE)
                ))
                .createdAt(record.get(USER.CREATED_AT))
                .updatedAt(record.get(USER.UPDATED_AT))
                .build();
    }
}
