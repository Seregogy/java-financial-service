ALTER TABLE loan_application
    ALTER COLUMN status SET DEFAULT 'NONE';

ALTER TABLE application_history
    ALTER COLUMN old_status SET DEFAULT 'NONE',
    ALTER COLUMN new_status SET DEFAULT 'NONE';