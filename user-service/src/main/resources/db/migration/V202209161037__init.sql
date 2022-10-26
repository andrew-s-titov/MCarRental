CREATE TABLE IF NOT EXISTS users
(
    id              UUID         NOT NULL,
    email           VARCHAR(50)  NOT NULL,
    password        VARCHAR(200) NOT NULL,
    email_confirmed BOOLEAN DEFAULT FALSE,
    created_date    TIMESTAMP    NOT NULL,
    phone           VARCHAR(13)  NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    active          BOOLEAN DEFAULT TRUE,

    first_name      VARCHAR(30),
    last_name       VARCHAR(30),

    reg_number      VARCHAR(9),
    company_type    VARCHAR(50),
    name            VARCHAR(100),
    legal_address   VARCHAR(200),
    pickup_address  VARCHAR(200),

    PRIMARY KEY (id),
    UNIQUE (email),
    UNIQUE (reg_number)
);

CREATE INDEX IF NOT EXISTS users_email_index ON users (email);
CREATE INDEX IF NOT EXISTS users_role_index ON users (role);
CREATE INDEX IF NOT EXISTS owner_reg_number_index ON users (reg_number);

CREATE TABLE IF NOT EXISTS email_verifications
(
    id           UUID        NOT NULL,
    user_id      UUID        NOT NULL,
    code         VARCHAR(15) NOT NULL,
    created_date TIMESTAMP   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (user_id),
    UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS email_verifications_user_id_index ON email_verifications (user_id);
CREATE INDEX IF NOT EXISTS email_verifications_code_index ON email_verifications (code);

CREATE TABLE IF NOT EXISTS password_reset_tokens
(
    id           UUID        NOT NULL,
    user_id      UUID        NOT NULL,
    code         VARCHAR(20) NOT NULL,
    created_date TIMESTAMP   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (user_id),
    UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS password_reset_user_id_index ON password_reset_tokens (user_id);
CREATE INDEX IF NOT EXISTS password_reset_code_index ON password_reset_tokens (code);

COMMIT;