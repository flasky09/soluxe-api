-- Module: Employees
-- employees depends on departments (V1)
-- attendance_records depends on employees + users (V2)
-- leave_requests depends on employees + leave_types (V3) + users (V2)

CREATE TABLE IF NOT EXISTS employees (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(255),
    phone           VARCHAR(255) NOT NULL UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    department_id   BIGINT,
    designation     VARCHAR(255),
    languages_spoken VARCHAR(255),
    basic_salary    DECIMAL(19, 2),
    date_of_joining DATE,
    nationality     VARCHAR(255),
    id_type         VARCHAR(50),
    id_number       VARCHAR(255) NOT NULL UNIQUE,
    kra_pin         VARCHAR(255),
    is_active       TINYINT(1) NOT NULL DEFAULT 1,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_employees_department FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE IF NOT EXISTS attendance_records (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT,
    date        DATE,
    clock_in    TIME,
    clock_out   TIME,
    hours_worked DECIMAL(19, 2),
    status      VARCHAR(50),
    notes       TEXT,
    recorded_by BIGINT,
    created_at  DATETIME(6),
    updated_at  DATETIME(6),
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_attendance_user     FOREIGN KEY (recorded_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS leave_requests (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id     BIGINT,
    leave_type_id   BIGINT,
    date_from       DATE,
    date_to         DATE,
    reason          VARCHAR(255),
    status          VARCHAR(50),
    approved_by     BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT fk_leave_employee  FOREIGN KEY (employee_id)   REFERENCES employees (id),
    CONSTRAINT fk_leave_type      FOREIGN KEY (leave_type_id) REFERENCES leave_types (id),
    CONSTRAINT fk_leave_approver  FOREIGN KEY (approved_by)   REFERENCES users (id)
);
