package ru.jsms.backend.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.common.entity.BaseEntity;
import ru.jsms.backend.files.entity.FileMetadataEntity;
import ru.jsms.backend.user.entity.OfferArticleVersion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
public class OfferArticleAnswer extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "version_id")
    private OfferArticleVersion offerArticleVersion;

    @ManyToOne
    @JoinColumn(name = "documents_archive_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private FileMetadataEntity document;
    @Column(name = "documents_archive_uuid")
    private UUID documentId;

    private String comment;

    @Builder.Default
    private boolean isDraft = true;
}
