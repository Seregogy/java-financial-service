CREATE TABLE "user" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    surname VARCHAR(255),
    patronymic VARCHAR(255),
    role VARCHAR(64),

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE user_additional_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE,
    birthday DATE,
    passport_series VARCHAR(4),
    passport_number VARCHAR(6),
    monthly_income DOUBLE PRECISION,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE car (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    brand VARCHAR(255),
    model VARCHAR(255),
    year INTEGER,
    cost DOUBLE PRECISION,

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE loan_application (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    car_id UUID UNIQUE,
    user_id UUID,

    loan_amount DOUBLE PRECISION,
    first_payment DOUBLE PRECISION,
    term_month INTEGER,

    status VARCHAR(64),

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE application_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID,

    old_status VARCHAR(64),
    new_status VARCHAR(64),

    created_at TIMESTAMP,
    changed_at TIMESTAMP,

    FOREIGN KEY (application_id) REFERENCES loan_application(id) ON DELETE CASCADE
);