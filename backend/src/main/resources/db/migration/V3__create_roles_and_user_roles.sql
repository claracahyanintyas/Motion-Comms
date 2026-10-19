CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGSERIAL NOT NULL,
    role_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);