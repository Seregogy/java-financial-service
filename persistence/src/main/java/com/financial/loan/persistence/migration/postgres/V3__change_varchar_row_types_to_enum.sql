CREATE TYPE user_role AS ENUM ('USER', 'MODERATOR', 'ADMIN');

CREATE TYPE loan_application_status AS ENUM ('NONE', 'NEW', 'IN_PROGRESS', 'APPROVED', 'REJECTED', 'EXPIRED');

ALTER TABLE "user"
    ALTER COLUMN role TYPE user_role USING role::user_role;

ALTER TABLE loan_application
    ALTER COLUMN status TYPE loan_application_status USING status::loan_application_status;

ALTER TABLE application_history
    ALTER COLUMN old_status TYPE loan_application_status USING old_status::loan_application_status,
    ALTER COLUMN new_status TYPE loan_application_status USING new_status::loan_application_status;