CREATE TABLE IF NOT EXISTS role (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_auth (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS user_roles (
    user_id VARCHAR(255),
    role_id VARCHAR(255),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user_auth(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
    );

CREATE TABLE IF NOT EXISTS credential (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE,
    hash TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_auth(id)
    );

CREATE TABLE IF NOT EXISTS session (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    expire_at TIMESTAMP NOT NULL,
    user_agent VARCHAR(255),
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    created_by_ip VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    replaced_by VARCHAR(255),
    revoked_by_ip VARCHAR(255),
    revoked_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_auth(id),
    FOREIGN KEY (replaced_by) REFERENCES session(id)
    );

CREATE TABLE IF NOT EXISTS verification (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expire_at TIMESTAMP NOT NULL,
    type VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_auth(id)
    );

CREATE INDEX idx_user_auth_email ON user_auth(email);
CREATE INDEX idx_credential_user_id ON credential(user_id);
CREATE INDEX idx_session_user_id ON session(user_id);
CREATE INDEX idx_verification_user_id ON verification(user_id);

INSERT INTO role (id, name, description, created_at) VALUES ('01J2MXVHQCARKGCJ0ZQ2YRD1PR', 'ROLE_ADMIN', 'Administrator with full access', NOW());
INSERT INTO role (id, name, description, created_at) VALUES ('01J2MXW00K7X30XXAK7JB45PGY', 'ROLE_USER', 'Standard user with limited access', NOW());

INSERT INTO user_auth (id, first_name, last_name, email, status, created_at, updated_at) VALUES
    ('01J2N1XP9M1Q4YK2YPMQE78V1E', 'Admin', null, '${ADMIN_EMAIL}', 'ACTIVE', NOW(), null);

INSERT INTO credential (id, user_id, hash, created_at, updated_at) VALUES
    ('02J3O2YP9N2Q5ZL3ZPMQE78V2F', '01J2N1XP9M1Q4YK2YPMQE78V1E', '${ADMIN_PASSWORD}', NOW(), null);

INSERT INTO user_roles (user_id, role_id) VALUES
    ('01J2N1XP9M1Q4YK2YPMQE78V1E', '01J2MXVHQCARKGCJ0ZQ2YRD1PR');
