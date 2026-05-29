CREATE TABLE "user" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(255),
	surname VARCHAR(255),
	patronymic VARCHAR(255),
	role VARCHAR(64),

	created_at DATETIME,
	updated_at DATETIME
);

CREATE TABLE "user_additional_data" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	user_id INT UNIQUE,
	birthday DATE,
	passport_series VARCHAR(4),
	passport_number VARCHAR(6),
	monthly_income DOUBLE,

	created_at DATETIME,
	updated_at DATETIME,

	FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE "car" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	brand varchar(255),
	model varchar(255),
	year INT,
	cost DOUBLE,

	created_at DATETIME,
	updated_at DATETIME
);

CREATE TABLE "loan_application" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	car_id INT UNIQUE,
	user_id INT,

	loan_amount DOUBLE,
	first_payment DOUBLE,
	term_month INT,

	status varchar(64),

	created_at DATETIME,
	updated_at DATETIME,

	FOREIGN KEY (car_id) REFERENCES car(id),
	FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE "application_history" (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	application_id INTEGER,

	old_status varchar(64),
	new_status varchar(64),

	created_at DATETIME,
	changed_at DATETIME,

	FOREIGN KEY (application_id) REFERENCES loan_application(id)
);