CREATE TABLE offer_article_version
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    owner_id          BIGINT REFERENCES users(user_id),
    created           TIMESTAMP WITHOUT TIME ZONE,
    updated           TIMESTAMP WITHOUT TIME ZONE,
    deleted           BOOLEAN NOT NULL,
    offer_article_id  BIGINT REFERENCES offer_article (id),
    article_archive   VARCHAR(255),
    documents_archive VARCHAR(255),
    comment           TEXT,
    is_draft          BOOLEAN DEFAULT TRUE NOT NULL
);
