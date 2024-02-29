CREATE TABLE refresh_tokens
(
    user_id     BIGINT                      NOT NULL,
    token       VARCHAR(255),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (user_id)
);