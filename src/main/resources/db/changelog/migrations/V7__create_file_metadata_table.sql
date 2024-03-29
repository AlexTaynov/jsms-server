CREATE TABLE file_metadata
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    uuid     UUID UNIQUE NOT NULL,
    owner_id BIGINT REFERENCES users(user_id) NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE,
    updated  TIMESTAMP WITHOUT TIME ZONE,
    deleted  BOOLEAN NOT NULL,
    name     VARCHAR(255) NOT NULL,
    size     BIGINT  NOT NULL
);

ALTER TABLE offer_article_version
    RENAME COLUMN article_archive TO article_archive_uuid;
ALTER TABLE offer_article_version
    ALTER COLUMN article_archive_uuid TYPE UUID USING (article_archive_uuid::UUID);

ALTER TABLE offer_article_version
    RENAME COLUMN documents_archive TO documents_archive_uuid;
ALTER TABLE offer_article_version
    ALTER COLUMN documents_archive_uuid TYPE UUID USING (documents_archive_uuid::UUID);

ALTER TABLE offer_article_version
    ADD CONSTRAINT FK_OFFER_ARTICLE_VERSION_ON_ARTICLE_ARCHIVE FOREIGN KEY (article_archive_uuid) REFERENCES file_metadata (uuid);
ALTER TABLE offer_article_version
    ADD CONSTRAINT FK_OFFER_ARTICLE_VERSION_ON_DOCUMENTS_ARCHIVE FOREIGN KEY (documents_archive_uuid) REFERENCES file_metadata (uuid);
ALTER TABLE offer_article_version
    ALTER COLUMN owner_id SET NOT NULL;