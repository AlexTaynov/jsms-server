package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "offer_article_version")
public class OfferArticleVersion extends BaseOwneredEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "offer_article_id", insertable = false, updatable = false)
    private OfferArticle offerArticle;
    @Column(name = "offer_article_id")
    private Long offerArticleId;

    private String articleArchive;

    private String documentsArchive;

    private String comment;

    @Builder.Default
    private boolean isDraft = true;
}
