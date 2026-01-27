-- 테이블 삭제 (자식 부터)
DROP TABLE IF EXISTS change_log_diff CASCADE;
DROP TABLE IF EXISTS change_logs CASCADE;
DROP TABLE IF EXISTS backup_histories CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS files CASCADE;

DROP TYPE IF EXISTS employee_status;
DROP TYPE IF EXISTS change_type;

CREATE TYPE employee_status AS ENUM ('ACTIVE', 'ON_LEAVE', 'RESIGNED');
CREATE TYPE change_type AS ENUM ('CREATED', 'UPDATED', 'DELETED');

-- 테이블 생성 (부모 부터)
CREATE TABLE files
(
    id              SERIAL PRIMARY KEY,
    file_name       VARCHAR(255) NOT NULL,
    content_type    VARCHAR(255) NOT NULL,
    file_size       BIGINT NOT NULL,
    file_path       VARCHAR(255),
    created_at      TIMESTAMPTZ NOT NULL,
    updated_at      TIMESTAMPTZ NOT NULL
);

CREATE TABLE departments
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR(50) NOT NULL UNIQUE,
    description         VARCHAR(255),
    established_date    DATE NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ NOT NULL
);

CREATE TABLE employees
(
    id                  SERIAL PRIMARY KEY,
    employee_number     VARCHAR(20) NOT NULL UNIQUE,
    name                VARCHAR(50) NOT NULL,
    email               VARCHAR(100) NOT NULL UNIQUE,
    position            VARCHAR(50) NOT NULL,
    hire_date           DATE NOT NULL,
    status              employee_status NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ NOT NULL,
    file_id             BIGINT REFERENCES files(id) ON DELETE CASCADE,
    department_id       BIGINT REFERENCES departments(id) ON DELETE SET NULL
);

CREATE TABLE change_log
(
    id                  SERIAL PRIMARY KEY,
    target_id           BIGINT NOT NULL,
    employee_name       VARCHAR(50) NOT NULL,
    employee_number     VARCHAR(50) NOT NULL,
    memo                VARCHAR(255),
    type                change_type NOT NULL,
    ip_address          VARCHAR(45) NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ NOT NULL
);

CREATE TABLE change_log_diffs
(
    id              SERIAL PRIMARY KEY,
    field_name      VARCHAR(50) NOT NULL,
    before_value    TEXT,
    after_value     TEXT,
    log_id          BIGINT REFERENCES change_log(id) ON DELETE CASCADE
);

CREATE TABLE db_backup_history
(
    id          SERIAL PRIMARY KEY,
    status      VARCHAR(20) NOT NULL CHECK (status IN ('IN_PROGRESS', 'COMPLETED', 'FAILED', 'SKIPPED')),
    worker      VARCHAR(50) NOT NULL,
    started_at  TIMESTAMPTZ NOT NULL,
    ended_at    TIMESTAMPTZ,
    file_id     BIGINT REFERENCES files(id) ON DELETE SET NULL
);
