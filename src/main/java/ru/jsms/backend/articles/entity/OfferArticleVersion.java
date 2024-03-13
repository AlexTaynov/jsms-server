package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.common.entity.BaseOwneredEntity;
import ru.jsms.backend.files.entity.FileMetadataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "offer_article_version")
public class OfferArticleVersion extends BaseOwneredEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "offer_article_id")
    private OfferArticle offerArticle;

    @ManyToOne
    @JoinColumn(name = "article_archive_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private FileMetadataEntity articleArchive;
    @Column(name = "article_archive_uuid")
    private UUID articleArchiveId;

    @ManyToOne
    @JoinColumn(name = "documents_archive_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private FileMetadataEntity documentsArchive;
    @Column(name = "documents_archive_uuid")
    private UUID documentsArchiveId;

    private String comment;

    @Builder.Default
    private boolean isDraft = true;
}
