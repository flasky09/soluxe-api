-- Employee Module: Departments, Employees, Attendance, and Leave

-- Departments
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Employees
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50) UNIQUE NOT NULL,
    designation VARCHAR(255),
    department_id BIGINT,
    basic_salary DECIMAL(19, 2),
    date_of_joining DATE,
    languages_spoken VARCHAR(255),
    nationality VARCHAR(100),
    id_type VARCHAR(50),
    id_number VARCHAR(100) UNIQUE NOT NULL,
    kra_pin VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Attendance Records
CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    date DATE NOT NULL,
    clock_in TIME,
    clock_out TIME,
    hours_worked DECIMAL(5, 2),
    status VARCHAR(50),
    notes TEXT,
    recorded_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Leave Types
CREATE TABLE IF NOT EXISTS leave_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Leave Requests
CREATE TABLE IF NOT EXISTS leave_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type_id BIGINT NOT NULL,
    date_from DATE NOT NULL,
    date_to DATE NOT NULL,
    reason TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    approved_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT fk_leave_type FOREIGN KEY (leave_type_id) REFERENCES leave_types(id)
);
