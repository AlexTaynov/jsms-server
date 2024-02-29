CREATE TABLE users
(
    user_id  BIGINT NOT NULL,
    password VARCHAR(255),
    roles    VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);