package ru.jsms.backend.admin.entity;

import lombok.AllArgsConstructor;
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
public class Review extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "version_id", insertable = false, updatable = false)
    private OfferArticleVersion offerArticleVersion;
    @Column(name = "version_id")
    private Long versionId;

    private String antiPlagiarism;

    @ManyToOne
    @JoinColumn(name = "first_reviewer_file_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private FileMetadataEntity firstReviewerFile;
    @Column(name = "first_reviewer_file_uuid")
    private UUID firstReviewerFileId;

    @ManyToOne
    @JoinColumn(name = "second_reviewer_file_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private FileMetadataEntity secondReviewerFile;
    @Column(name = "second_reviewer_file_uuid")
    private UUID secondReviewerFileId;
}
