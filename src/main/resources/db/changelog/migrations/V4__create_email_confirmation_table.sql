CREATE TABLE email_confirmation
(
    email       VARCHAR(255) PRIMARY KEY    NOT NULL,
    code        UUID UNIQUE                 NOT NULL,
    confirmed   BOOLEAN DEFAULT FALSE,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);